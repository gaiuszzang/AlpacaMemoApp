package com.crash.alpaca

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object AlpacaBindingAdapter {

    @JvmStatic
    @BindingAdapter("bindRecyclerViewAdapter")
    fun bindRecyclerViewAdapter(rcView: RecyclerView, adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        rcView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("bindLongClickListener")
    fun bindLongClickListener(view: View, listener: View.OnLongClickListener) {
        view.setOnLongClickListener(listener)
    }
}