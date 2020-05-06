package com.crash.alpaca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.R
import com.crash.alpaca.databinding.MemoRoomItemLayoutBind
import com.crash.alpaca.databinding.MemoRoomListFragmentBind
import com.crash.alpaca.db.AlpacaRepository

//ViewModel
class MemoRoomListFragmentViewModel : ViewModel() {
    val memoRoomAdapter: MemoRoomListAdapter = MemoRoomListAdapter()

    fun loadMemoRooms(): LiveData<List<MemoRoom>> {
        return AlpacaRepository.alpacaDao().getAllMemoRoomList()
    }
}

//MemoRoomList Adapter
class MemoRoomListAdapter : RecyclerView.Adapter<MemoRoomListAdapter.MemoRoomViewHolder>() {
    private val itemList = mutableListOf<MemoRoom>()

    //Inner Holder
    inner class MemoRoomViewHolder(
        private val bind: MemoRoomItemLayoutBind
    ) : RecyclerView.ViewHolder(bind.root) {

        fun bind(item: MemoRoom) {
            bind.memoRoom = item
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoRoomListAdapter.MemoRoomViewHolder {
        return MemoRoomViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.layout_memoroomitem, parent, false))
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MemoRoomViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun updateList(list: List<MemoRoom>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }
}

//Fragment
class MemoRoomListFragment : Fragment() {
    companion object {
        private const val TAG = "MemoRoomListFragment"
    }

    lateinit var bind: MemoRoomListFragmentBind

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // setting ViewModel
        val vm: MemoRoomListFragmentViewModel by viewModels()
        vm.loadMemoRooms().observe(viewLifecycleOwner, Observer {
            vm.memoRoomAdapter.updateList(it)
        })

        // setting DataBinding
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_memoroomlist, container, false)
        bind.apply {
            lifecycleOwner = viewLifecycleOwner
            this.vm = vm
        }
        return bind.root
    }
}

