package com.crash.alpaca.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.crash.alpaca.R
import com.crash.alpaca.databinding.DialogFragmentBind

abstract class AlpacaDialogFragment : DialogFragment() {

    private var content: ViewGroup? = null
    private lateinit var mBind: DialogFragmentBind
    var negativeButtonClickListener = { _: View -> dismiss() }
    var positiveButtonClickListener = { _: View -> dismiss() }
    @StringRes
    var titleResId: Int = 0

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
        if (titleResId != 0) {
            dialog?.setTitle(titleResId)
        }
        mBind = DataBindingUtil.inflate(
                inflater, R.layout.dialog, container, false)
        return mBind.apply {
            onNegativeClickListener = View.OnClickListener(negativeButtonClickListener)
            onPositiveClickListener = View.OnClickListener(positiveButtonClickListener)
            content = frame
            lifecycleOwner = this@AlpacaDialogFragment
        }.root
    }

    fun setContentView(view: View, showNegBtn: Boolean, showPosBtn: Boolean) {
        content?.addView(view)
        if (::mBind.isInitialized) {
            mBind.showNegativeButton = showNegBtn;
            mBind.showPositiveButton = showPosBtn;
        }
    }

    abstract fun show(manager: FragmentManager)
}