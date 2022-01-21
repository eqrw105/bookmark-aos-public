package com.nims.bookmark.ui

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.nims.bookmark.NotNullMutableLiveData
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path
import java.util.*

class RegisterViewModel(private val repository: RegisterRepositoryImpl): ViewModel() {
    private val _folders: NotNullMutableLiveData<List<Folder>> = NotNullMutableLiveData(arrayListOf())
    val folders: LiveData<List<Folder>> get() = _folders

    fun createPath(v: View, title: String, url: String, folder: Any) {
        if (folder !is Folder) {
            return
        }
        val date = Calendar.getInstance().time.time
        val path = Path(
            title = title,
            url = url,
            folderId = folder.id,
            date = date,
            lastUpdate = date
        )
        repository.insertPath(path)
        Snackbar.make(v, "$title 등록 완료", Snackbar.LENGTH_SHORT).show()
    }

    fun createFolder(v: View, title: String) {
        val date = Calendar.getInstance().time.time
        val folder = Folder(
            title = title,
            date = date,
            lastUpdate = date
        )
        repository.insertFolder(folder)
        Snackbar.make(v, "$title 등록 완료", Snackbar.LENGTH_SHORT).show()
    }

    fun fetchFolders() {
        _folders.postValue(repository.getFolders())
    }
}