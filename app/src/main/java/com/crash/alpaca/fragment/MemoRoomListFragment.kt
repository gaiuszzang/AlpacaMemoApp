package com.crash.alpaca.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.crash.alpaca.Alpaca
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.R
import com.crash.alpaca.databinding.MemoRoomItemLayoutBind
import com.crash.alpaca.databinding.MemoRoomListFragmentBind
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.android.synthetic.main.fragment_memoroomlist.*
import kotlinx.coroutines.*

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
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

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

        //Toolbar
        (activity as AppCompatActivity).setSupportActionBar(bind.toolbar)
        setHasOptionsMenu(true)
        return bind.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.memoroomlist_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuAdd -> createNewMemoRoom()
        }
        return super.onOptionsItemSelected(item)
    }

    fun createNewMemoRoom() {
        scope.launch {
            val ioThread = if (Alpaca.DEBUG) Dispatchers.Main else Dispatchers.IO
            val memoRoomId = async(ioThread) {
                return@async AlpacaRepository.alpacaDao().getMaxMemoRoomId() + 1
            }.await()
            val memoRoom = MemoRoom(
                memoRoomId,
                "New Room" + memoRoomId,
                "Description " + memoRoomId,
                MemoRoom.TYPE_NORMAL,
                MemoRoom.VISIBILITY_SHOW
            )
            async(ioThread) {
                AlpacaRepository.alpacaDao().insertMemoRoom(memoRoom)
            }.await()
            Toast.makeText(context, "Test Add Memo ID " + memoRoomId, Toast.LENGTH_SHORT).show()
        }
    }
}

