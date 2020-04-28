package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.crash.alpaca.R
import com.crash.alpaca.databinding.MemoRoomListFragmentBind

class MemoRoomListFragmentViewModel : ViewModel() {
    val testStr = "Hello, MemoRoom List Fragment!"
}

class MemoRoomListFragment : Fragment() {

    lateinit var bind: MemoRoomListFragmentBind

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val vm: MemoRoomListFragmentViewModel by viewModels()

        return DataBindingUtil.inflate<MemoRoomListFragmentBind>(inflater,
                R.layout.fragment_memoroomlist, container, false).apply {
            this.vm = vm
        }.root
    }
}

