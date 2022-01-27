package com.nims.bookmark.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.nims.bookmark.R
import com.nims.bookmark.library.NotNullMutableLiveData
import com.nims.bookmark.library.PrefUtil
import com.nims.bookmark.repository.RepositoryImpl
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path
import com.nims.bookmark.ui.detail.DetailActivity
import java.util.*

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

    fun getFolderList(): List<Folder> {
        return repository.getFolders()
    }

    fun createPath(path: Path) {
        repository.insertPath(path)
    }

    fun createFolder(folder: Folder) {
        repository.insertFolder(folder)
    }

    fun getNewFolderId() = repository.getFoldersWhereDate().last().id

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

    fun deletePath(v: View, path: Path) {
        openAlert(v.context, path,
            successCallback = {
                repository.deletePath(path)
                fetchPaths(PrefUtil.selectedFolderId)
            },
            failedCallback = {}
        )
    }

    private fun openAlert(
        context: Context,
        item: Path,
        successCallback: () -> Unit,
        failedCallback: () -> Unit
    ) {
        AlertDialog.Builder(context).apply {
            setTitle(item.title)
            setMessage(context.getString(R.string.main_path_delete_message))
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

    val folderLongClickListener = View.OnLongClickListener {
        val context = it.context
        (context as? MainActivity)?.openEdit()
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