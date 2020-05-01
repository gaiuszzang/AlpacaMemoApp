package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.R
import com.crash.alpaca.databinding.MemoRoomItemLayoutBind
import com.crash.alpaca.databinding.MemoRoomListFragmentBind

//ViewModel
class MemoRoomListFragmentViewModel : ViewModel() {
    val memoRoomAdapter: MemoRoomListAdapter = MemoRoomListAdapter()
}

//MemoRoomList Adapter
class MemoRoomListAdapter: RecyclerView.Adapter<MemoRoomListAdapter.MemoRoomViewHolder>() {
    private val itemList: ArrayList<MemoRoom> = ArrayList()

    //Inner Holder
    inner class MemoRoomViewHolder(private val bind: MemoRoomItemLayoutBind): RecyclerView.ViewHolder(bind.root) {
        fun bind(item: MemoRoom) {
            bind.apply {
                memoRoom = item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoRoomListAdapter.MemoRoomViewHolder {
        return MemoRoomViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.layout_memoroomitem, parent,false))
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MemoRoomViewHolder, position: Int) {
        holder.apply {
            bind(itemList[position])
        }
    }

    fun updateList(list: List<MemoRoom>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }
}

//Fragment
class MemoRoomListFragment : Fragment() {
    private val TAG: String = "MemoRoomListFragment"
    lateinit var bind: MemoRoomListFragmentBind
    //TODO : Test Function
    fun getTestMemoRoomList(): List<MemoRoom> {
        val list : ArrayList<MemoRoom> = ArrayList()
        list.add(
            MemoRoom(
                0,
                "Test Room 0",
                "This is Description of Room 0"
            )
        )
        list.add(
            MemoRoom(
                1,
                "Test Room 1",
                "This is Description of Room 1"
            )
        )
        list.add(
            MemoRoom(
                2,
                "Test Room 2",
                "This is Description of Room 2"
            )
        )
        list.add(
            MemoRoom(
                3,
                "Test Room 3",
                "This is Description of Room 3"
            )
        )
        list.add(
            MemoRoom(
                4,
                "Test Room 4",
                "This is Description of Room 4"
            )
        )
        list.add(
            MemoRoom(
                5,
                "Test Room 5",
                "This is Description of Room 5"
            )
        )
        return list
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // setting ViewModel
        val vm: MemoRoomListFragmentViewModel by viewModels()
        vm.memoRoomAdapter.updateList(getTestMemoRoomList())

        // setting DataBinding
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_memoroomlist, container, false)
        bind.apply {
            lifecycleOwner = this@MemoRoomListFragment
            this.vm = vm
        }
        /* TODO : Load List from DB
        var thread = Thread({
            val db : AlpacaDatabase = Alpaca.instance.db
            vm.memoRoomAdapter.updateList(db.alpacaDao().getAllMemoRoomList())
        })
        thread.start() */
        return bind.root
    }
}

