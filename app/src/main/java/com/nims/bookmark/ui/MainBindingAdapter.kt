package com.nims.bookmark.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.nims.bookmark.library.dp2px
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path

@BindingAdapter(value = ["paths", "viewModel"])
fun setPaths(view: RecyclerView, items: MutableList<Path>, viewModel: MainViewModel) {
    view.adapter?.run {
        if (this is PathAdapter) {
            this.items = items
        }
    }?: run {
        PathAdapter(viewModel).apply {
            this.items = items
            view.adapter = this
            view.addItemDecoration(ItemVerticalDecoration(view.context, dp2px(20f), dp2px(1f)))
            val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(this))
            itemTouchHelper.attachToRecyclerView(view)
        }
    }
}

@BindingAdapter(value = ["folders"])
fun setFolders(view: TabLayout, items: List<Folder>) {
    view.removeAllTabs()
    items.forEach {
        view.apply {
            addTab(this.newTab().setTag(it.id).setText(it.title))
        }
    }
}