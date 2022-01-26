package com.nims.bookmark.ui.edit

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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

    override fun onItemSwiped(v: View, position: Int) {
        val context = v.context
        val item = items[position]
        if (item.id == 1) {
            Snackbar.make(
                v,
                context.getString(R.string.edit_default_folder_delete),
                Snackbar.LENGTH_SHORT
            ).show()
            notifyItemChanged(position)
            return
        }
        openAlert(context, item,
            successCallback = {
                viewModel.deleteFolder(items[position])
                items.removeAt(position)
                notifyItemRemoved(position)
                viewModel.putResult(context)
            },
            failedCallback = {
                notifyItemChanged(position)
            }
        )
    }

    private fun openAlert(
        context: Context,
        item: Folder,
        successCallback: () -> Unit,
        failedCallback: () -> Unit
    ) {
        AlertDialog.Builder(context).apply {
            setTitle(item.title)
            setMessage(context.getString(R.string.main_folder_delete_message))
            setPositiveButton(context.getString(R.string.common_delete)) { _, _ ->
                successCallback()
            }
            setNegativeButton(context.getString(R.string.common_cancel)) { _, _ ->
                failedCallback()
            }
            setOnCancelListener {
                failedCallback()
            }
        }
            .create()
            .show()
    }
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