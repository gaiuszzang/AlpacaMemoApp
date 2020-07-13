package com.crash.alpaca.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.crash.alpaca.R
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.databinding.MemoRoomItemLayoutBind

class MainAdapter : RecyclerView.Adapter<MainAdapter.MemoRoomViewHolder>() {

    private val itemList = mutableListOf<MemoRoom>()
    private val itemSelected = mutableListOf<MemoRoom>()

    var onItemClickListener: (MemoRoom) -> Unit = {}
    var onItemLongClickListener: (MemoRoom) -> Unit = {}

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): MemoRoomViewHolder {
        return MemoRoomViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_memoroomitem, parent, false))
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MemoRoomViewHolder, position: Int) {
        val memoRoom = itemList[position]
        val selected = itemSelected.contains(memoRoom)

        holder.bind(itemList[position], selected)
    }

    override fun getItemId(position: Int) : Long {
        return itemList[position].id.toLong()
    }

    fun updateList(rooms: List<MemoRoom>) {
        itemList.clear()
        itemList.addAll(rooms)
        notifyDataSetChanged()
    }

    fun updateSelections(rooms: List<MemoRoom>) {
        itemSelected.clear()
        itemSelected.addAll(rooms)
        notifyDataSetChanged()
    }

    inner class MemoRoomViewHolder(
            private val bind: MemoRoomItemLayoutBind
    ) : RecyclerView.ViewHolder(bind.root) {

        fun bind(item: MemoRoom, isSelected: Boolean) {
            bind.memoRoom = item
            bind.isSelected = isSelected
            bind.setItemClickListener {
                onItemClickListener.invoke(item)
            }
            bind.setItemLongClickListener {
                onItemLongClickListener.invoke(item)
                true
            }
        }
    }
}
