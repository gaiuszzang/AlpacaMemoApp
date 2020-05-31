package com.crash.alpaca.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.crash.alpaca.R
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.databinding.MemoRoomItemLayoutBind

class MemoRoomListAdapter : RecyclerView.Adapter<MemoRoomListAdapter.MemoRoomViewHolder>() {
    private val itemList = mutableListOf<MemoRoom>()
    private val itemSelected = mutableListOf<Boolean>()
    private var itemSelectedCount = 0
    private var isSelectMode = false

    var onItemClickListener: (MemoRoom) -> Unit = {}
    var onSelectModeChangedListener: (Boolean) -> Unit = {}

    inner class MemoRoomViewHolder(
            private val bind: MemoRoomItemLayoutBind
    ) : RecyclerView.ViewHolder(bind.root) {

        fun bind(pos: Int, item: MemoRoom, isSelected: Boolean) {
            bind.memoRoom = item
            bind.isSelected = isSelectMode && isSelected
            bind.setItemClickListener {
                if (isSelectMode) {
                    toggleItemSelected(pos)
                    if (itemSelectedCount == 0) setSelectMode(false)
                } else {
                    onItemClickListener.invoke(item)
                }
            }
            bind.setItemLongClickListener {
                if (!isSelectMode) {
                    setSelectMode(true)
                    toggleItemSelected(pos)
                }
                return@setItemLongClickListener true
            }
        }
    }

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
        holder.bind(position, itemList[position], itemSelected[position])
    }

    override fun getItemId(position: Int) : Long {
        return itemList[position].id.toLong()
    }

    fun updateList(list: List<MemoRoom>) {
        itemList.clear()
        itemList.addAll(list)
        clearItemSelected()
        notifyDataSetChanged()
    }

    fun setSelectMode(isOn: Boolean) {
        isSelectMode = isOn
        if (!isSelectMode) {
            clearItemSelected()
        }
        notifyDataSetChanged()
        onSelectModeChangedListener.invoke(isSelectMode)
    }

    fun getSelectMode() = isSelectMode

    fun getSelectItemList() = itemList.filterIndexed { index, _ -> itemSelected[index] }

    private fun clearItemSelected() {
        itemSelected.clear()
        itemSelected.addAll(Array(itemList.size) { false })
        itemSelectedCount = 0
    }

    private fun toggleItemSelected(pos: Int) {
        itemSelected[pos] = !itemSelected[pos]
        itemSelectedCount = if (itemSelected[pos]) itemSelectedCount + 1 else itemSelectedCount - 1
        notifyItemChanged(pos)
    }
}
