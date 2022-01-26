package com.nims.bookmark.ui.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingViewHolder
import com.nims.bookmark.databinding.ItemFolderBinding
import com.nims.bookmark.library.ItemTouchHelperListener
import com.nims.bookmark.room.Folder

class FolderAdapter(private val viewModel: EditViewModel) :
    RecyclerView.Adapter<FolderViewHolder>(),
    ItemTouchHelperListener {
    var items: MutableList<Folder> = arrayListOf()
        set(value) {
            val callback = FolderDiffCallback(field, value)
            field = value
            val result = DiffUtil.calculateDiff(callback)
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val item = items[position].apply {
            holder.binding.item = this
            holder.binding.viewModel = viewModel
        }
    }

    override fun getItemCount(): Int = items.size


    override fun onItemMoved(v: View, from: Int, to: Int) {
        viewModel.updateFolder(v, items[from], items[to])
        val fromItem = items.removeAt(from)
        items.add(to, fromItem)
        notifyItemMoved(from, to)
    }

    override fun onItemSwiped(v: View, position: Int) {}

}

class FolderViewHolder(view: View) : BindingViewHolder<ItemFolderBinding>(view)

class FolderDiffCallback(private val oldList: List<Folder>, private val newList: List<Folder>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}