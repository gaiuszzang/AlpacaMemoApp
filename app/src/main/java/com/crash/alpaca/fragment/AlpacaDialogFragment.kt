package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.crash.alpaca.R
import com.crash.alpaca.databinding.LayoutAlpacaDialogBinding

abstract class AlpacaDialogFragment : DialogFragment() {

    private var content: ViewGroup? = null

    lateinit var mBind: LayoutAlpacaDialogBinding
    var negativeButtonClickListener = { _: View -> dismiss() }
    var positiveButtonClickListener = { _: View -> dismiss() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog_Title)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_dialog)

        mBind = DataBindingUtil.inflate<LayoutAlpacaDialogBinding>(
            inflater, R.layout.layout_alpaca_dialog, container, false)
        return mBind.apply {
            onNegativeClickListener = View.OnClickListener(negativeButtonClickListener)
            onPositiveClickListener = View.OnClickListener(positiveButtonClickListener)
            content = frame
        }.root
    }

    fun setContentView(view: View, showNegBtn: Boolean, showPosBtn: Boolean) {
        content?.addView(view)
        mBind.showNegativeButton = showNegBtn;
        mBind.showPositiveButton = showPosBtn;
    }

    abstract fun show(manager: FragmentManager)
}