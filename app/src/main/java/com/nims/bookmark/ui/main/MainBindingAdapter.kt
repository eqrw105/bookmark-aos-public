package com.nims.bookmark.ui.main

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.nims.bookmark.library.ItemTouchHelperCallback
import com.nims.bookmark.library.ItemVerticalDecoration
import com.nims.bookmark.library.PrefUtil
import com.nims.bookmark.library.dp2px
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path

@BindingAdapter(value = ["paths", "viewModel"])
fun setPaths(view: RecyclerView, items: MutableList<Path>, viewModel: MainViewModel) {
    view.adapter?.run {
        if (this is PathAdapter) {
            val isSame = this.items.size == items.size && this.items.containsAll(items)
            this.items = items
            if (!isSame) {
                view.post { view.smoothScrollToPosition(0) }
            }
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
                this.getChildAt(it.index)?.setOnLongClickListener(viewModel.folderLongClickListener)
            }
            //탭 선택했던 기록 자동 선택
            if (it.value.id == PrefUtil.selectedFolderId) {
                newTab.select()
            }
        }
    }
}

@BindingAdapter(value = ["listener"])
fun setScrollListener(view: RecyclerView, listener: RecyclerView.OnScrollListener) {
    view.removeOnScrollListener(listener)
    view.addOnScrollListener(listener)
}