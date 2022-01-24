package com.nims.bookmark.ui

import android.view.ViewGroup
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
    } ?: run {
        PathAdapter(viewModel).apply {
            this.items = items
            view.adapter = this
            view.addItemDecoration(ItemVerticalDecoration(view.context, dp2px(20f), dp2px(1f)))
            val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(this))
            itemTouchHelper.attachToRecyclerView(view)
        }
    }
}

@BindingAdapter(value = ["folders", "viewModel"])
fun setFolders(view: TabLayout, items: List<Folder>, viewModel: MainViewModel) {
    view.removeAllTabs()
    items.withIndex().forEach {
        view.apply {
            val newTab = this.newTab().setTag(it.value.id).setText(it.value.title)
            addTab(newTab)
            (this.getChildAt(0) as? ViewGroup)?.run {
                this.getChildAt(it.index)?.setOnLongClickListener(viewModel.folderDeleteListener)
            }
        }
    }
}

@BindingAdapter(value = ["listener"])
fun setScrollListener(view: RecyclerView, listener: RecyclerView.OnScrollListener) {
    view.removeOnScrollListener(listener)
    view.addOnScrollListener(listener)
}