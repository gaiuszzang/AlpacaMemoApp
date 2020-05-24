package com.crash.alpaca.fragment

import android.os.Bundle
import android.util.Log
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
import com.crash.alpaca.data.Memo
import com.crash.alpaca.databinding.MemoRoomFragmentBind
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.*
import java.util.*

class MemoRoomFragment(private val roomId: Int, private val roomTitle: String) : Fragment() {
    companion object {
        val TAG = "MemoRoomFragment"
    }

    private val vm: MemoRoomFragmentViewModel by viewModels()
    lateinit var bind: MemoRoomFragmentBind

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val ioThread = if (Alpaca.DEBUG) Dispatchers.Main else Dispatchers.IO


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Setting ViewModel
        vm.context = this.requireContext()
        vm.loadMemo(roomId).observe(viewLifecycleOwner, Observer {
            vm.updateItems(it)
            scrollToLastItem()
        })
        if (!vm.memoRoomAdapter.hasObservers()) {
            vm.memoRoomAdapter.setHasStableIds(true) //Don't remove.
        }
        vm.plusClickListener = {
            scrollToLastItem() //Temp
        }
        vm.writeClickListener = {
            addMemo()
        }

        // Setting Bind
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_memoroom, container, false)
        bind.lifecycleOwner = this
        bind.vm = vm
        setSupportActionBar(bind.toolbar)

        // Setting BackButton to ActionBark
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(roomTitle)

        return bind.root
    }

    fun addMemo() {
        scope.launch {
            val content = vm.userMsg.value?: ""
            if (!content.equals("")) {
                val msgId =  UUID.randomUUID().toString()
                async(ioThread) {
                    val memo = Memo(msgId, roomId, content, System.currentTimeMillis())
                    AlpacaRepository.alpacaDao().insertMemo(memo)
                }.await()
                vm.userMsg.value = "";
            }
        }
    }

    fun scrollToLastItem() {
        bind.rvMemoList.scrollToPosition(vm.memoRoomAdapter.itemCount - 1)
        postScrollToLastItem()
    }
    // There is one issue that when itemview height is different, scrolling not working well.
    // so I add the postScroll Method
    fun postScrollToLastItem() {
        bind.rvMemoList.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                bind.rvMemoList.scrollToPosition(vm.memoRoomAdapter.itemCount - 1)
                bind.rvMemoList.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}