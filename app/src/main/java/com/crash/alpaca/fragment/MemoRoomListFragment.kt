package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.crash.alpaca.R
import com.crash.alpaca.databinding.MemoRoomListFragmentBind

class MemoRoomListFragmentViewModel : ViewModel() {
    public val testStr = "Hello, MemoRoom List Fragment!"
}

class MemoRoomListFragment : Fragment() {

    lateinit var bind:MemoRoomListFragmentBind
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val vm = ViewModelProviders.of(this).get(MemoRoomListFragmentViewModel::class.java)
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_memoroomlist, container, false)
        bind.vm = vm
        return bind.root
    }
}

