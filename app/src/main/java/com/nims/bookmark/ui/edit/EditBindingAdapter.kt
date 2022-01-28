package com.nims.bookmark.ui.edit

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nims.bookmark.library.ItemTouchHelperCallback
import com.nims.bookmark.library.ItemVerticalDecoration
import com.nims.bookmark.library.dp2px
import com.nims.bookmark.listener.OnEditFolderClickListener
import com.nims.bookmark.room.Folder

@BindingAdapter(value = ["folders", "viewModel", "itemListener"])
fun setFolders(view: RecyclerView, items: MutableList<Folder>, viewModel: EditViewModel, itemListener: OnEditFolderClickListener) {
    view.adapter?.run {
        if (this is FolderAdapter) {
            this.items = items
        }
    } ?: run {
        FolderAdapter(viewModel, itemListener).apply {
            this.items = items
            view.adapter = this
            view.addItemDecoration(ItemVerticalDecoration(view.context, dp2px(20f), dp2px(1f)))
            val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(this))
            itemTouchHelper.attachToRecyclerView(view)
        }
    }
}