package com.nims.bookmark.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path

@BindingAdapter(value = ["paths", "viewModel"])
fun setPaths(view: RecyclerView, items: List<Path>, viewModel: MainViewModel) {
    view.adapter?.run {
        if (this is PathAdapter) {
            this.items = items
        }
    }?: run {
        PathAdapter(viewModel).apply {
            this.items = items
            view.adapter = this
        }
    }
    view.scrollToPosition(0)
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