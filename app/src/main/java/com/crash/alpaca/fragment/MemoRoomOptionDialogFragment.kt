package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.crash.alpaca.R
import com.crash.alpaca.databinding.MemoRoomOptionDialogFragmentBind
import com.crash.alpaca.viewmodel.MemoRoomOptionDialogViewModel

class MemoRoomOptionDialogFragment : AlpacaDialogFragment() {
    companion object {
        private const val TAG = "MemoRoomOptionDialogFragment"
    }
    private val viewModel: MemoRoomOptionDialogViewModel by viewModels()
    private lateinit var bind: MemoRoomOptionDialogFragmentBind
    private lateinit var resultCallback: (title: String, desc: String) -> Unit

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle(R.string.create_memo_room_title)

        bind = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_memo_room_option, container, false)
        bind.vm = viewModel

        positiveButtonClickListener = listener@ {
            if (viewModel.roomTitle.value.equals("") || viewModel.roomDesc.value.equals("")) {
                Toast.makeText(requireContext(), "Please Input Title & Description", Toast.LENGTH_SHORT).show()
                return@listener
            }
            resultCallback(viewModel.roomTitle.value!!, viewModel.roomDesc.value!!)
            dismiss()
        }

        return super.onCreateView(inflater, container, savedInstanceState).apply {
            setContentView(bind.root, showNegBtn = true, showPosBtn = true)
        }
    }

    override fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    fun setResultCallback(cb: (title: String, desc: String) -> Unit) {
        resultCallback = cb
    }
}