package com.crash.alpaca.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.crash.alpaca.R
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.databinding.MemoRoomDialogFragmentBind
import com.crash.alpaca.fragment.showToast
import com.crash.alpaca.viewmodel.MemoRoomDialogViewModel

class MemoRoomDialogFragment : AlpacaDialogFragment() {
    companion object {
        private const val TAG = "MemoRoomOptionDialogFragment"
        const val ARG_ROOM_ID = "ARG_ROOM_ID"
        private const val UNKNOWN_ID = -1
    }
    private val viewModel: MemoRoomDialogViewModel by viewModels()
    private lateinit var bind: MemoRoomDialogFragmentBind
    lateinit var resultCallback: (memoRoom: MemoRoom) -> Unit

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater,
                R.layout.dialog_memoroom, container, false)
        bind.vm = viewModel
        bind.lifecycleOwner = this

        titleResId = R.string.create_memo_room_title
        arguments?.getInt(ARG_ROOM_ID, UNKNOWN_ID)?.let { roomId ->
            if (roomId != UNKNOWN_ID) {
                viewModel.loadMemoRoom(this, roomId)
                titleResId = R.string.update_memo_room_title
            }
        }

        positiveButtonClickListener = listener@ {
            viewModel.saveMemoRoom saveMemoRoom@{ memoRoom, errMsg ->
                if (memoRoom == null && errMsg != null) {
                    showToast(errMsg)
                    return@saveMemoRoom
                }
                if (::resultCallback.isInitialized) {
                    resultCallback(memoRoom!!)
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