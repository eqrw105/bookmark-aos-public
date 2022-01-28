package com.nims.bookmark.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.nims.bookmark.library.NotNullMutableLiveData
import com.nims.bookmark.repository.RepositoryImpl
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path

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

    fun insertPath(path: Path) {
        repository.insertPath(path)
    }

    fun deletePath(path: Path) {
        repository.deletePath(path)
    }

    fun insertFolder(folder: Folder) {
        repository.insertFolder(folder)
    }

    fun getNewFolderId() = repository.getFoldersWhereDate().last().id

    fun updatePath(fromItem: Path, toItem: Path) {
        val tempLastUpdate = fromItem.lastUpdate
        fromItem.lastUpdate = toItem.lastUpdate
        toItem.lastUpdate = tempLastUpdate
        repository.updatePath(fromItem)
        repository.updatePath(toItem)
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