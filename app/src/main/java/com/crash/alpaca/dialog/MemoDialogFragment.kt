package com.crash.alpaca.dialog

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
import com.crash.alpaca.databinding.MemoDialogFragmentBind
import com.crash.alpaca.fragment.showToast
import com.crash.alpaca.viewmodel.MemoDialogViewModel

class MemoDialogFragment : AlpacaDialogFragment() {
    companion object {
        private const val TAG = "MemoDialogFragment"
    }
    private val viewModel: MemoDialogViewModel by viewModels()
    private lateinit var bind: MemoDialogFragmentBind
    lateinit var memo: Memo

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        viewModel.init(memo)

        bind = DataBindingUtil.inflate(inflater, R.layout.dialog_memo, container, false)
        bind.vm = viewModel
        bind.v = this
        bind.lifecycleOwner = this

        return super.onCreateView(inflater, container, savedInstanceState).apply {
            setContentView(bind.root, showNegBtn = false, showPosBtn = false)
        }
    }

    override fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    fun itemClick(index: Int) {
        when(index) {
            0 -> {
                viewModel.copyMemoToClipboard()
                showToast("Copy to Clipboard")
            }
            1, 2 -> showAlarmDialog()
            3 -> viewModel.removeAlarm()
            4 -> viewModel.removeMemo()
        }
        dismiss()
    }

    private fun showAlarmDialog() {
        AlarmDialogFragment().apply {
            memo = viewModel.memo.value!!
            alarm = viewModel.alarm.value
        }.show(parentFragmentManager)
    }
}