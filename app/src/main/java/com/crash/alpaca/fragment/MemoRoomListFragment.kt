package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
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
import kotlinx.coroutines.*

class MemoRoomListFragment : Fragment() {
    companion object {
        private const val TAG = "MemoRoomListFragment"
    }
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val ioThread = if (Alpaca.DEBUG) Dispatchers.Main else Dispatchers.IO
    private val viewModel: MemoRoomListFragmentViewModel by viewModels()
    lateinit var menu: Menu
    lateinit var bind: MemoRoomListFragmentBind
    private val memoRoomListAdapter = MemoRoomListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Setting Adapter
        if (!memoRoomListAdapter.hasObservers()) {
            memoRoomListAdapter.setHasStableIds(true) //Don't remove.
        }
        memoRoomListAdapter.onItemClickListener = {
            val memoRoomFrag = MemoRoomFragment()
            val args = Bundle()
            args.putInt("roomId", it.id)
            memoRoomFrag.arguments = args
            setFragment(memoRoomFrag)
        }
        memoRoomListAdapter.onSelectModeChangedListener = {
            updateActionBarMenu(it)
        }

        // Setting ViewModel
        viewModel.apply {
            loadMemoRooms().observe(viewLifecycleOwner, Observer {
                memoRoomListAdapter.updateList(it)
            })
        }
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_memoroomlist, container, false)
        bind.apply {
            lifecycleOwner = viewLifecycleOwner
            rvMemoRoomList.adapter = memoRoomListAdapter
            vm = viewModel
        }
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
            R.id.menuRemove -> removeMemoRoomSelected()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSelectMode(): Boolean {
        return memoRoomListAdapter.getSelectMode()
    }

    private fun setSelectMode(isOn: Boolean) {
        memoRoomListAdapter.setSelectMode(isOn)
    }

    private fun createNewMemoRoom() = scope.launch {
        val newMemoRoomId = withContext(ioThread) { viewModel.createNewMemoRoom("New Memo Room", "This is new memo room") }
        Toast.makeText(requireContext(), "Created MemoRoomId : $newMemoRoomId", Toast.LENGTH_SHORT).show()
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

