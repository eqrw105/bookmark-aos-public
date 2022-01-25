package com.nims.bookmark.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingViewHolder
import com.nims.bookmark.databinding.ItemPathBinding
import com.nims.bookmark.library.ItemTouchHelperListener
import com.nims.bookmark.room.Path

class PathAdapter(private val viewModel: MainViewModel) : RecyclerView.Adapter<PathViewHolder>(),
    ItemTouchHelperListener {
    var items: MutableList<Path> = arrayListOf()
        set(value) {
            val callback = PathDiffCallback(field, value)
            field = value
            val result = DiffUtil.calculateDiff(callback)
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PathViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_path, parent, false)
        return PathViewHolder(view)
    }

    override fun onBindViewHolder(holder: PathViewHolder, position: Int) {
        val item = items[position].apply {
            holder.binding.item = this
            holder.binding.viewModel = viewModel
        }
    }

    override fun getItemCount(): Int = items.size


    override fun onItemMoved(from: Int, to: Int) {
        viewModel.updatePath(items[from], items[to])
        val fromItem = items.removeAt(from)
        items.add(to, fromItem)
        notifyItemMoved(from, to)
    }

    override fun onItemSwiped(position: Int) {
        viewModel.deletePath(items[position])
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}

class PathViewHolder(view: View) : BindingViewHolder<ItemPathBinding>(view)

class PathDiffCallback(private val oldList: List<Path>, private val newList: List<Path>) :
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