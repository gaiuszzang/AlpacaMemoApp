package com.crash.alpaca

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object AlpacaBindingAdapter {

    @JvmStatic
    @BindingAdapter("bindRecyclerViewAdapter")
    fun bindRecyclerViewAdapter(rcView: RecyclerView, adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        rcView.adapter = adapter
    }
}