package com.crash.alpaca.fragment

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
import com.crash.alpaca.databinding.MemoRoomOptionDialogFragmentBind
import com.crash.alpaca.viewmodel.MemoRoomOptionDialogViewModel

class MemoRoomOptionDialogFragment : AlpacaDialogFragment() {
    companion object {
        private const val TAG = "MemoRoomOptionDialogFragment"

        const val ARG_ROOM_ID = "ARG_ROOM_ID"
        private const val UNKNOWN_ID = -1
    }
    private val viewModel: MemoRoomOptionDialogViewModel by viewModels()
    private lateinit var bind: MemoRoomOptionDialogFragmentBind
    private lateinit var resultCallback: (title: String, desc: String, type: Int, hidden: Int) -> Unit

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater,
                R.layout.layout_dialog_memo_room_option, container, false)
        bind.vm = viewModel

        arguments?.getInt(ARG_ROOM_ID, UNKNOWN_ID)?.let { roomId ->
            viewModel.loadMemoRoom(roomId).observe(viewLifecycleOwner, Observer {
                bind.title = it?.title
                bind.description = it?.description
                bind.typeNormal = it?.roomType == MemoRoom.TYPE_NORMAL
                bind.typeDiary = it?.roomType == MemoRoom.TYPE_DIARY
                bind.hidden = it?.hidden == MemoRoom.VISIBILITY_HIDDEN
            })
        }

        positiveButtonClickListener = listener@ {
            val title = bind.title ?: ""
            val description = bind.description ?: ""
            val type = if (bind.typeNormal) MemoRoom.TYPE_NORMAL else MemoRoom.TYPE_DIARY
            val hidden = if (bind.hidden) MemoRoom.VISIBILITY_HIDDEN else MemoRoom.VISIBILITY_SHOW
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Please Input Title & Description",
                        Toast.LENGTH_SHORT).show()
                return@listener
            }
            if (::resultCallback.isInitialized) {
                resultCallback(title, description, type, hidden)
            }
            dismiss()
        }

        return super.onCreateView(inflater, container, savedInstanceState).apply {
            setContentView(bind.root, showNegBtn = true, showPosBtn = true)
        }
    }

    override fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    fun setResultCallback(cb: (title: String, desc: String, type: Int, hidden: Int) -> Unit) {
        resultCallback = cb
    }
}