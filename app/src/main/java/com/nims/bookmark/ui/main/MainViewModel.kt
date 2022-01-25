package com.nims.bookmark.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.nims.bookmark.R
import com.nims.bookmark.library.NotNullMutableLiveData
import com.nims.bookmark.repository.RepositoryImpl
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path
import com.nims.bookmark.ui.detail.DetailActivity

class MainViewModel(private val repository: RepositoryImpl) : ViewModel() {
    private val _paths: NotNullMutableLiveData<List<Path>> = NotNullMutableLiveData(arrayListOf())
    val paths: LiveData<List<Path>> get() = _paths

    private val _folders: NotNullMutableLiveData<List<Folder>> =
        NotNullMutableLiveData(arrayListOf())
    val folders: LiveData<List<Folder>> get() = _folders

    private val _scrollTopState: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val scrollTopState: LiveData<Boolean> get() = _scrollTopState

    fun fetchPaths(folderId: Int) {
        _paths.postValue(repository.getPaths(folderId))
    }

    fun fetchFolders() {
        _folders.postValue(repository.getFolders())
    }

    fun openRegister(v: View) {
        val context = v.context
        (context as? MainActivity)?.openRegister()
    }

    fun openPath(v: View, item: Path) {
        val context = v.context
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.PATH_ITEM_KEY, item)
        context.startActivity(intent)
    }

    fun updatePath(fromItem: Path, toItem: Path) {
        val tempLastUpdate = fromItem.lastUpdate
        fromItem.lastUpdate = toItem.lastUpdate
        toItem.lastUpdate = tempLastUpdate
        repository.updatePath(fromItem)
        repository.updatePath(toItem)
    }

    fun deletePath(path: Path) {
        repository.deletePath(path)
    }

    val folderDeleteListener = View.OnLongClickListener {
        val context = it.context
        (it as? TabLayout.TabView)?.tab?.let { tab ->
            AlertDialog.Builder(context).apply {
                val folderId = tab.tag
                if (folderId == 1) {
                    return@OnLongClickListener true
                }
                setTitle(tab.text)
                setMessage(context.getString(R.string.main_folder_delete_message))
                setPositiveButton(context.getString(R.string.main_folder_delete)) { _, _ ->
                    (folderId as? Int)?.run {
                        repository.deleteFolder(this)
                        fetchFolders()
                        Snackbar.make(
                            it,
                            context.getString(R.string.main_folder_delete_success),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                setNegativeButton(context.getString(R.string.main_folder_delete_cancel), null)
            }
                .create()
                .show()
        }
        return@OnLongClickListener true
    }

    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (recyclerView.computeVerticalScrollOffset() > 0) {
                if (!_scrollTopState.value) {
                    _scrollTopState.postValue(true)
                }
            } else {
                if (_scrollTopState.value) {
                    _scrollTopState.postValue(false)
                }
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }
}