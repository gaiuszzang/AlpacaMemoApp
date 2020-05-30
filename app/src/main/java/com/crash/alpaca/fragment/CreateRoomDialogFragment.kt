package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.crash.alpaca.R
import com.crash.alpaca.databinding.LayoutDialogCreateRoomBinding

class CreateRoomDialogFragment : AlpacaDialogFragment() {

    companion object {
        private const val TAG = "CreateRoomDialogFragment"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle(R.string.create_memo_room_title)

        return super.onCreateView(inflater, container, savedInstanceState).apply {
            setContentView(DataBindingUtil.inflate<LayoutDialogCreateRoomBinding>(
                    inflater, R.layout.layout_dialog_create_room, container, false
            ).root)
        }
    }

    override fun show(manager: FragmentManager) {
        show(manager, TAG)
    }
}