package com.crash.alpaca.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.crash.alpaca.R
import com.crash.alpaca.data.Memo
import com.crash.alpaca.databinding.MemoItemLayoutBind

class MemoRoomAdapter : RecyclerView.Adapter<MemoRoomAdapter.MemoRoomViewHolder>() {
    private val itemList = mutableListOf<Memo>()
    private val itemSelected = mutableListOf<Boolean>()
    private var isSelectMode = false

    var onItemClickListener: (Memo) -> Unit = {}
    var onSelectModeChangedListener: (Boolean) -> Unit = {}

    inner class MemoRoomViewHolder(private val bind: MemoItemLayoutBind) : RecyclerView.ViewHolder(bind.root) {

        fun bind(pos: Int, item: Memo, isSelected: Boolean) {
            bind.memo = item
            bind.isSelected = isSelectMode && isSelected
            bind.setItemClickListener {
                if (isSelectMode) {
                    itemSelected[pos] = !itemSelected[pos]
                    notifyItemChanged(pos)
                } else {
                    onItemClickListener.invoke(item)
                }
            }
            bind.setItemLongClickListener {
                //TODO : UX is not fixed
                /*if (!isSelectMode) {
                    //itemSelected[pos] = true
                    //setSelectMode(true)
                }*/
                return@setItemLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoRoomAdapter.MemoRoomViewHolder {
        return MemoRoomViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.layout_memoitem, parent, false))
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MemoRoomViewHolder, position: Int) {
        holder.bind(position, itemList[position], itemSelected[position])
    }

    override fun getItemId(position: Int) : Long {
        //return itemList[position].id.toLong() //TODO
        return itemList[position].time;
    }

    fun updateList(list: List<Memo>) {
        itemList.clear()
        itemList.addAll(list)
        itemSelected.clear()
        itemSelected.addAll(Array(list.size){false})
        notifyDataSetChanged()
    }

    fun setSelectMode(isOn: Boolean) {
        isSelectMode = isOn
        if (!isSelectMode) {
            itemSelected.clear()
            itemSelected.addAll(Array(itemList.size) { false })
        }
        notifyDataSetChanged()
        onSelectModeChangedListener.invoke(isSelectMode)
    }

    fun getSelectMode() = isSelectMode

    fun getSelectItemList() = itemList.filterIndexed { index, _ -> itemSelected[index] }
}
