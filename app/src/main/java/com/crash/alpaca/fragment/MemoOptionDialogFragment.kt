package com.crash.alpaca.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.crash.alpaca.R
import com.crash.alpaca.data.Memo
import com.crash.alpaca.databinding.MemoOptionDialogFragmentBind
import com.crash.alpaca.viewmodel.MemoOptionDialogViewModel

class MemoOptionDialogFragment : AlpacaDialogFragment() {
    companion object {
        private const val TAG = "MemoOptionDialogFragment"
    }
    private val viewModel: MemoOptionDialogViewModel by viewModels()
    private lateinit var bind: MemoOptionDialogFragmentBind
    private lateinit var memo: Memo
    private lateinit var resultCallback: (index: Int) -> Unit

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        viewModel.init(memo)

        bind = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_memo_option, container, false)
        bind.vm = viewModel
        bind.v = this

        return super.onCreateView(inflater, container, savedInstanceState).apply {
            setContentView(bind.root, showNegBtn = false, showPosBtn = false)
        }
    }

    fun setMemo(memo: Memo) {
        this.memo = memo
    }

    override fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    fun setResultCallback(cb: (index: Int) -> Unit) {
        resultCallback = cb
    }

    fun itemClick(index: Int) {
        Log.d(TAG, "itemClick : $index")
        resultCallback(index)
        dismiss()
    }
}