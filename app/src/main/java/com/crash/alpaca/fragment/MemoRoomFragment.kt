package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.crash.alpaca.Alpaca
import com.crash.alpaca.R
import com.crash.alpaca.adapter.MemoRoomAdapter
import com.crash.alpaca.databinding.MemoRoomFragmentBind
import com.crash.alpaca.logd
import com.crash.alpaca.viewmodel.MemoRoomFragmentViewModel
import kotlinx.coroutines.*

class MemoRoomFragment : Fragment() {
    companion object {
        private const val TAG = "MemoRoomFragment"
    }

    interface MemoRoomFragmentCallback {
        fun onClickAddMemo()
        fun onClickPlus()
    }

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val ioThread = if (Alpaca.DEBUG) Dispatchers.Main else Dispatchers.IO
    val memoRoomAdapter = MemoRoomAdapter()
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
                    scope.launch {
                        val content = viewModel.userMsg.value!!
                        withContext(ioThread) {
                            logd(TAG, "added memo")
                            viewModel.addMemo(content)
                        }
                        viewModel.userMsg.value = ""
                    }
                }
                override fun onClickPlus() {
                    scrollToLastItem()
                }
            }
        }

        // Setting ActionBar
        setSupportActionBar(bind.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return bind.root
    }

    fun scrollToLastItem() {
        bind.rvMemoList.scrollToPosition(memoRoomAdapter.itemCount - 1)
        postScrollToLastItem()
    }
    // There is one issue that when itemview height is different, scrolling not working well.
    // so I add the postScroll Method
    fun postScrollToLastItem() {
        bind.rvMemoList.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                bind.rvMemoList.scrollToPosition(memoRoomAdapter.itemCount - 1)
                bind.rvMemoList.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}