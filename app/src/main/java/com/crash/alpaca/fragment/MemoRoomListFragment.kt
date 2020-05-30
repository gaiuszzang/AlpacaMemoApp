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
import com.crash.alpaca.adapter.MemoRoomListAdapter
import com.crash.alpaca.databinding.MemoRoomListFragmentBind
import com.crash.alpaca.setting.SettingFragment
import com.crash.alpaca.viewmodel.MemoRoomListFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoRoomListFragment : Fragment() {

    companion object {
        private const val TAG = "MemoRoomListFragment"
    }

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val ioThread = if (Alpaca.DEBUG) Dispatchers.Main else Dispatchers.IO
    private val viewModel: MemoRoomListFragmentViewModel by viewModels()
    private lateinit var menu: Menu
    private val memoRoomListAdapter = MemoRoomListAdapter().apply {
        onItemClickListener = {
            setFragment(MemoRoomFragment().apply {
                arguments = Bundle().apply {
                    putInt("roomId", it.id)
                }
            })
        }
        onSelectModeChangedListener = {
            updateActionBarMenu(it)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Setting Adapter
        if (!memoRoomListAdapter.hasObservers()) {
            memoRoomListAdapter.setHasStableIds(true) //Don't remove.
        }
        // Setting ViewModel
        viewModel.loadMemoRooms().observe(viewLifecycleOwner, Observer {
            memoRoomListAdapter.updateList(it)
        })

        setHasOptionsMenu(true)

        setBackKeyPressCallback {
            if (getSelectMode()) {
                setSelectMode(false)
                true
            } else {
                false
            }
        }

        val bind = DataBindingUtil.inflate<MemoRoomListFragmentBind>(
                inflater, R.layout.fragment_memoroomlist, container, false).apply {
            setSupportActionBar(toolbar)
            lifecycleOwner = viewLifecycleOwner
            rvMemoRoomList.adapter = memoRoomListAdapter
            vm = viewModel
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
        return when (item.itemId) {
            R.id.menuAdd -> {
                createNewMemoRoom()
                true
            }
            R.id.menuSetting -> {
                setFragment(SettingFragment())
                true
            }
            R.id.menuRemove -> {
                removeMemoRoomSelected()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun getSelectMode(): Boolean {
        return memoRoomListAdapter.getSelectMode()
    }

    private fun setSelectMode(isOn: Boolean) {
        memoRoomListAdapter.setSelectMode(isOn)
    }

    private fun createNewMemoRoom() {
        CreateRoomDialogFragment().apply {
            positiveButtonClickListener = {
                scope.launch {
                    val roomId = withContext(ioThread) {
                        viewModel.createNewMemoRoom("New Memo Room", "This is new memo room")
                    }
                    setFragment(MemoRoomFragment().apply {
                        arguments = Bundle().apply {
                            putInt("roomId", roomId)
                        }
                    })
                    dismiss()
                }
            }
        }.show(parentFragmentManager)
    }

    private fun updateActionBarMenu(isSelectMode: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isSelectMode)
        menu.findItem(R.id.menuAdd)?.isVisible = !isSelectMode
        menu.findItem(R.id.menuSetting)?.isVisible = !isSelectMode
        menu.findItem(R.id.menuRemove)?.isVisible = isSelectMode
    }

    private fun removeMemoRoomSelected() = scope.launch {
        withContext(ioThread) {
            memoRoomListAdapter.getSelectItemList().forEach {
                viewModel.removeMemoRoom(it)
            }
        }
        setSelectMode(false)
    }
}

