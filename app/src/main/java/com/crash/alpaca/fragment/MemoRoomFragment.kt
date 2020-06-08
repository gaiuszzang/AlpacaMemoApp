package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.crash.alpaca.R
import com.crash.alpaca.adapter.MemoRoomAdapter
import com.crash.alpaca.data.Memo
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.databinding.MemoRoomFragmentBind
import com.crash.alpaca.db.AlpacaRepository
import com.crash.alpaca.viewmodel.MemoRoomFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoRoomFragment : Fragment() {
    companion object {
        private const val TAG = "MemoRoomFragment"
    }

    interface MemoRoomFragmentCallback {
        fun onClickAddMemo()
        fun onClickPlus()
    }

    val memoRoomAdapter = MemoRoomAdapter().apply {
        onItemLongClickListener = {
            showMemoOption(it)
        }
    }
    private val viewModel: MemoRoomFragmentViewModel by viewModels()
    lateinit var bind: MemoRoomFragmentBind
    private var roomId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        roomId = requireArguments().getInt("roomId", -1)

        // Setting Adapter
        if (!memoRoomAdapter.hasObservers()) {
            memoRoomAdapter.setHasStableIds(true) //Don't remove.
        }

        // Setting ViewModel
        viewModel.apply {
            context = requireContext()
            roomId = this@MemoRoomFragment.roomId
            loadMemo().observe(viewLifecycleOwner, Observer {
                memoRoomAdapter.updateList(it)
                scrollToLastItem()
            })
            loadMemoRoom().observe(viewLifecycleOwner, Observer {
                supportActionBar?.title = it?.title
            })
        }

        // Setting Bind
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_memoroom, container, false)
        bind.apply {
            lifecycleOwner = viewLifecycleOwner
            rvMemoList.adapter = memoRoomAdapter
            vm = viewModel
            callback = object : MemoRoomFragmentCallback {
                override fun onClickAddMemo() {
                    viewModel.addMemo()
                }
                override fun onClickPlus() {
                    scrollToLastItem()
                }
            }
        }
        // Setting ActionBar
        setSupportActionBar(bind.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)
        return bind.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.memo_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.meno_modify -> {
                MemoRoomOptionDialogFragment().apply {
                    titleResId = R.string.update_memo_room_title
                    arguments = Bundle().apply {
                        putInt(MemoRoomOptionDialogFragment.ARG_ROOM_ID, roomId)
                    }
                    setResultCallback { title, desc, type, hidden ->
                        CoroutineScope(Dispatchers.IO).launch {
                            AlpacaRepository.alpacaDao().updateMemoRoom(MemoRoom(roomId,
                                    title, desc, type, hidden))
                            withContext(Dispatchers.Main) {
                                supportActionBar?.title = title
                            }
                        }
                    }
                }.show(parentFragmentManager)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun showMemoOption(memo: Memo) {
        MemoOptionDialogFragment().apply {
            setMemo(memo)
            setResultCallback { index ->
                when(index) {
                    0 -> {
                        viewModel.copyMemoToClipboard(memo)
                        Toast.makeText(requireContext(), "Copy to Clipboard", Toast.LENGTH_SHORT).show()
                    }
                    1 -> viewModel.removeMemo(memo)
                }
            }
        }.show(parentFragmentManager)
    }

    fun scrollToLastItem() {
        bind.rvMemoList.scrollToPosition(memoRoomAdapter.itemCount - 1)
        postScrollToLastItem()
    }

    // There is one issue that when itemview height is different, scrolling not working well.
    // so I add the postScroll Method
    private fun postScrollToLastItem() {
        bind.rvMemoList.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                bind.rvMemoList.scrollToPosition(memoRoomAdapter.itemCount - 1)
                bind.rvMemoList.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}