package com.crash.alpaca.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.crash.alpaca.R
import com.crash.alpaca.data.Alarm
import com.crash.alpaca.data.Memo
import com.crash.alpaca.databinding.AlarmDialogFragmentBind
import com.crash.alpaca.fragment.showToast
import com.crash.alpaca.viewmodel.AlarmDialogViewModel

class AlarmDialogFragment : AlpacaDialogFragment() {
    companion object {
        private const val TAG = "AlarmDialogFragment"
    }
    private val viewModel: AlarmDialogViewModel by viewModels()
    private lateinit var bind: AlarmDialogFragmentBind

    lateinit var memo: Memo
    var alarm: Alarm? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        viewModel.init(memo, alarm)

        bind = DataBindingUtil.inflate(inflater, R.layout.dialog_alarm, container, false)
        bind.vm = viewModel
        bind.lifecycleOwner = this

        positiveButtonClickListener = listener@ {
            viewModel.saveAlarm saveAlarm@{ alarm, errMsg ->
                if (alarm == null && errMsg != null) {
                    showToast(errMsg)
                    return@saveAlarm
                }
                dismiss()
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState).apply {
            setContentView(bind.root, showNegBtn = true, showPosBtn = true)
        }
    }

    override fun show(manager: FragmentManager) {
        show(manager, TAG)
    }
}