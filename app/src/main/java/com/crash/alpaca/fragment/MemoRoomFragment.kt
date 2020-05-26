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
import com.crash.alpaca.R
import com.crash.alpaca.databinding.MemoRoomFragmentBind

class MemoRoomFragment() : Fragment() {
    companion object {
        val TAG = "MemoRoomFragment"
    }

    interface MemoRoomFragmentCallback {
        fun onClickAddMemo()
        fun onClickPlus()
    }

    val memoRoomAdapter = MemoRoomAdapter()
    private val viewModel: MemoRoomFragmentViewModel by viewModels()
    lateinit var bind: MemoRoomFragmentBind
    private var roomId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        roomId = requireArguments().getInt("roomId", -1)

        // Setting ViewModel
        viewModel.context = this.requireContext()
        viewModel.loadMemo(roomId).observe(viewLifecycleOwner, Observer {
            memoRoomAdapter.updateList(it)
            scrollToLastItem()
        })
        viewModel.loadMemoRoom(roomId).observe(viewLifecycleOwner, Observer {
            supportActionBar?.title = it?.title
        })
        if (!memoRoomAdapter.hasObservers()) {
            memoRoomAdapter.setHasStableIds(true) //Don't remove.
        }

        // Setting Bind
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_memoroom, container, false)
        bind.apply {
            lifecycleOwner = viewLifecycleOwner
            rvMemoList.adapter = memoRoomAdapter
            vm = viewModel
            callback = object : MemoRoomFragmentCallback {
                override fun onClickAddMemo() {
                    viewModel.addMemo(roomId)
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