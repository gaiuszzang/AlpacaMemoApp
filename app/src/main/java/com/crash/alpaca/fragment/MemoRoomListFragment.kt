package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.crash.alpaca.Alpaca
import com.crash.alpaca.R
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.databinding.MemoRoomListFragmentBind
import com.crash.alpaca.db.AlpacaRepository
import com.crash.alpaca.setting.SettingFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoRoomListFragment : Fragment() {
    companion object {
        private const val TAG = "MemoRoomListFragment"
    }

    private val vm: MemoRoomListFragmentViewModel by viewModels()
    lateinit var menu: Menu
    lateinit var bind: MemoRoomListFragmentBind

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val ioThread = if (Alpaca.DEBUG) Dispatchers.Main else Dispatchers.IO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm.loadMemoRooms().observe(viewLifecycleOwner, Observer {
            vm.updateItems(it)
        })
        vm.setOnItemClickListener {
            setFragment(MemoRoomFragment(it.id, it.title))
        }
        vm.setOnSelectModeChangedListener {
            updateActionBarMenu(it)
        }
        if (!vm.memoRoomListAdapter.hasObservers()) {
            vm.memoRoomListAdapter.setHasStableIds(true) //Don't remove.
        }

        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_memoroomlist, container, false)
        bind.lifecycleOwner = viewLifecycleOwner
        bind.vm = vm
        setSupportActionBar(bind.toolbar)

        setHasOptionsMenu(true)
        setBackKeyPressCallback {
            if (getSelectMode()) {
                setSelectMode(false)
                true
            } else {
                false
            }
        }

        return bind.root
    }

    override fun onPause() {
        super.onPause()
        setBackKeyPressCallback(null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.memoroomlist_menu, menu)
        updateActionBarMenu(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuAdd -> createNewMemoRoom()
            R.id.menuSetting -> setFragment(SettingFragment())
            R.id.menuRemove -> {
                scope.launch {
                    withContext(ioThread) {
                        removeMemoRoomSelected()
                    }
                    setSelectMode(false)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getSelectMode(): Boolean {
        return vm.getSelectMode()
    }

    fun setSelectMode(isOn: Boolean) {
        vm.setSelectMode(isOn)
    }

    private fun updateActionBarMenu(isSelectMode: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isSelectMode)
        menu.findItem(R.id.menuAdd)?.isVisible = !isSelectMode
        menu.findItem(R.id.menuSetting)?.isVisible = !isSelectMode
        menu.findItem(R.id.menuRemove)?.isVisible = isSelectMode
    }

    private fun createNewMemoRoom() {
        scope.launch {
            val memoRoomId = async(ioThread) {
                return@async AlpacaRepository.alpacaDao().getMaxMemoRoomId() + 1
            }.await()
            val memoRoom = MemoRoom(
                memoRoomId,
                "New Room $memoRoomId",
                "Description $memoRoomId",
                MemoRoom.TYPE_NORMAL,
                MemoRoom.VISIBILITY_SHOW
            )
            withContext(ioThread) {
                AlpacaRepository.alpacaDao().insertMemoRoom(memoRoom)
            }
        }
    }

    private fun removeMemoRoomSelected() {
        vm.getSelectItemList().forEach {
            AlpacaRepository.alpacaDao().deleteMemoRoom(it)
        }
    }
}

