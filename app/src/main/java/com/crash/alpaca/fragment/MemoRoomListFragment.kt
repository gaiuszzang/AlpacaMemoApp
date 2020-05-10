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
import com.crash.alpaca.R
import com.crash.alpaca.activity.MainActivity
import com.crash.alpaca.data.MemoRoom
import com.crash.alpaca.databinding.MemoRoomItemLayoutBind
import com.crash.alpaca.databinding.MemoRoomListFragmentBind
import com.crash.alpaca.db.AlpacaRepository
import kotlinx.coroutines.*

//ViewModel
class MemoRoomListFragmentViewModel : ViewModel() {
    interface MemoRoomViewModelCallback {
        fun onSelectModeChanged(isOn: Boolean)
        fun onItemClick(item: MemoRoom)
    }

    lateinit var vmCallback: MemoRoomViewModelCallback
    val memoRoomAdapter: MemoRoomListAdapter = MemoRoomListAdapter()

    fun addCallback(callback: MemoRoomViewModelCallback) {
        vmCallback = callback
        memoRoomAdapter.addCallback(vmCallback)
    }

    fun loadMemoRooms(): LiveData<List<MemoRoom>> {
        return AlpacaRepository.alpacaDao().getAllMemoRoomList()
    }

    fun setSelectMode(isOn: Boolean) = memoRoomAdapter.setSelectMode(isOn)

    fun getSelectMode(): Boolean = memoRoomAdapter.getSelectMode()

    fun getSelectItemList(): List<MemoRoom> = memoRoomAdapter.getSelectItemList()
}

//MemoRoomList Adapter
class MemoRoomListAdapter : RecyclerView.Adapter<MemoRoomListAdapter.MemoRoomViewHolder>() {
    private lateinit var vmCallback: MemoRoomListFragmentViewModel.MemoRoomViewModelCallback
    private val itemList = mutableListOf<MemoRoom>()
    private val itemSelected = mutableListOf<Boolean>()
    private var isSelectMode = false

    //Inner Holder
    inner class MemoRoomViewHolder(private val bind: MemoRoomItemLayoutBind) : RecyclerView.ViewHolder(bind.root) {
        fun bind(pos: Int, item: MemoRoom, isSelected: Boolean) {
            bind.memoRoom = item
            bind.isSelected = isSelectMode && isSelected
            bind.setItemClickListener {
                if (isSelectMode) {
                    itemSelected[pos] = (itemSelected[pos] != true) //toggle
                    notifyItemChanged(pos)
                } else {
                    vmCallback.onItemClick(item)
                }
            }
            bind.setItemLongClickListener {
                if (!isSelectMode) {
                    itemSelected[pos] = true
                    setSelectMode(true)
                }
                return@setItemLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoRoomListAdapter.MemoRoomViewHolder {
        return MemoRoomViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.layout_memoroomitem, parent, false))
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MemoRoomViewHolder, position: Int) {
        holder.bind(position, itemList[position], itemSelected[position])
    }

    override fun getItemId(position: Int) : Long {
        return itemList[position].id.toLong()
    }

    fun addCallback(callback: MemoRoomListFragmentViewModel.MemoRoomViewModelCallback) {
        vmCallback = callback
    }

    fun updateList(list: List<MemoRoom>) {
        itemList.clear()
        itemList.addAll(list)
        itemSelected.clear()
        itemSelected.addAll(Array(list.size){false})
        notifyDataSetChanged()
    }

    fun setSelectMode(isOn: Boolean) {
        isSelectMode = isOn
        if (isSelectMode == false) {
            itemSelected.clear()
            itemSelected.addAll(Array(itemList.size){false})
        }
        notifyDataSetChanged()
        vmCallback.onSelectModeChanged(isSelectMode)
    }

    fun getSelectMode(): Boolean = isSelectMode

    fun getSelectItemList(): List<MemoRoom> = itemList.filterIndexed { index, _ -> itemSelected[index] }
}

//Fragment
class MemoRoomListFragment : Fragment(), MemoRoomListFragmentViewModel.MemoRoomViewModelCallback, MainActivity.IBackPressCallback {
    companion object {
        private const val TAG = "MemoRoomListFragment"
    }

    lateinit var menu: Menu
    lateinit var bind: MemoRoomListFragmentBind
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // setting ViewModel
        val vm: MemoRoomListFragmentViewModel by viewModels()
        vm.loadMemoRooms().observe(viewLifecycleOwner, Observer {
            vm.memoRoomAdapter.updateList(it)
        })
        vm.memoRoomAdapter.setHasStableIds(true)
        vm.addCallback(this)

        // setting DataBinding
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_memoroomlist, container, false)
        bind.lifecycleOwner = viewLifecycleOwner
        bind.vm = vm

        // setting Toolbar
        (activity as AppCompatActivity).setSupportActionBar(bind.toolbar)
        setHasOptionsMenu(true)

        // setting BackPress Callback
        (activity as MainActivity).addBackPressCallback(this)

        return bind.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.memoroomlist_menu, menu)
        updateActionBarMenu(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuAdd -> createNewMemoRoom()
            R.id.menuSetting -> showSetting()
            R.id.menuRemove -> {
                scope.launch {
                    val ioThread = if (Alpaca.DEBUG) Dispatchers.Main else Dispatchers.IO
                    async(ioThread) {
                        removeMemoRoomSelected()
                    }.await()
                    setSelectMode(false)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSelectModeChanged(isOn: Boolean) {
        updateActionBarMenu(isOn)
    }

    override fun onBackPressed(): Boolean {
        if (getSelectMode()) {
            setSelectMode(false)
            return true;
        }
        return false;
    }

    override fun onItemClick(item: MemoRoom) {
        Toast.makeText(context, "MemoRoom (id:" + item.id + ") clicked", Toast.LENGTH_SHORT).show()
        //TODO Implement
    }

    fun getSelectMode(): Boolean = bind.vm?.getSelectMode()!!

    fun setSelectMode(isOn: Boolean) {
        bind.vm?.setSelectMode(isOn)!!
    }

    fun updateActionBarMenu(isSelectMode: Boolean) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(isSelectMode)
        val menuItemAdd = menu.findItem(R.id.menuAdd)
        val menuItemSetting = menu.findItem(R.id.menuSetting)
        val menuItemRemove = menu.findItem(R.id.menuRemove)
        menuItemAdd.setVisible(!isSelectMode)
        menuItemSetting.setVisible(!isSelectMode)
        menuItemRemove.setVisible(isSelectMode)
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
        }
    }

    fun showSetting() {
        Toast.makeText(context, "TBD", Toast.LENGTH_SHORT).show()
    }

    fun removeMemoRoomSelected() {
        bind.apply {
            vm?.getSelectItemList()!!.forEach {
                AlpacaRepository.alpacaDao().deleteMemoRoom(it.id)
            }
        }
    }

}

