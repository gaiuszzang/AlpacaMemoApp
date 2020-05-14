package com.crash.alpaca.fragment

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.crash.alpaca.R
import com.crash.alpaca.activity.MainActivity

fun Fragment.setSupportActionBar(toolbar: Toolbar) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }
}

val Fragment.supportActionBar: ActionBar?
    get() {
        context?.let {
            if (it is AppCompatActivity) {
                return it.supportActionBar
            }
        }
        return null
    }

fun Fragment.setBackKeyPressCallback(callback: (() -> Boolean)?) {
    if (activity is MainActivity) {
        (activity as MainActivity).backPressCallback = callback
    }
}

fun Fragment.setFragment(
    fragment: Fragment,
    addToBackStack: Boolean = true
) {
    parentFragmentManager.beginTransaction().apply {
        if (addToBackStack) {
            addToBackStack(null)
        }
        replace(R.id.lyFrame, fragment)
    }.commit()
}