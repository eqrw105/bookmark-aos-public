package com.nims.bookmark.ui

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nims.bookmark.NotNullMutableLiveData
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path

class MainViewModel(private val repository: RepositoryImpl): ViewModel() {
    private val _paths: NotNullMutableLiveData<List<Path>> = NotNullMutableLiveData(arrayListOf())
    val paths: LiveData<List<Path>> get() = _paths

    private val _folders: NotNullMutableLiveData<List<Folder>> = NotNullMutableLiveData(arrayListOf())
    val folders: LiveData<List<Folder>> get() = _folders

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
}