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
import com.crash.alpaca.R
import com.crash.alpaca.adapter.MainAdapter
import com.crash.alpaca.databinding.MainFragmentBind
import com.crash.alpaca.dialog.MemoRoomDialogFragment
import com.crash.alpaca.fragment.setting.SettingFragment
import com.crash.alpaca.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val memoRoomListAdapter = MainAdapter().apply {
        onItemClickListener = {
            viewModel.onClickMemoRoom(it)
        }
        onItemLongClickListener = {
            viewModel.onLongClickMemoRoom(it)
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
        viewModel.selectedRooms.observe(viewLifecycleOwner, Observer {
            memoRoomListAdapter.updateSelections(it)
        })
        viewModel.showMemoEvent.observe(viewLifecycleOwner, Observer {
            showMemoRoom(it)
        })

        setHasOptionsMenu(true)

        return DataBindingUtil.inflate<MainFragmentBind>(
                inflater, R.layout.fragment_main, container, false).apply {
            setSupportActionBar(toolbar)
            lifecycleOwner = viewLifecycleOwner
            rvMemoRoomList.adapter = memoRoomListAdapter
            vm = viewModel
        }.root
    }

    override fun onResume() {
        super.onResume()
        setBackKeyPressCallback {
            viewModel.onBackPress()
        }
    }

    override fun onPause() {
        super.onPause()
        setBackKeyPressCallback(null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.memoroomlist_menu, menu)

        viewModel.selectMode.observe(viewLifecycleOwner, Observer {
            supportActionBar?.setDisplayHomeAsUpEnabled(it)
            menu.findItem(R.id.menuAdd)?.isVisible = !it
            menu.findItem(R.id.menuSetting)?.isVisible = !it
            menu.findItem(R.id.menuRemove)?.isVisible = it
        })
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
                viewModel.removeMemoRooms()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun createNewMemoRoom() {
        MemoRoomDialogFragment().apply {
            resultCallback = { memoRoom ->
                showMemoRoom(memoRoom.id)
            }
        }.show(parentFragmentManager)
    }

    private fun showMemoRoom(roomId: Int) {
        setFragment(MemoRoomFragment().apply {
            arguments = Bundle().apply {
                putInt("roomId", roomId)
            }
        })
    }
}

