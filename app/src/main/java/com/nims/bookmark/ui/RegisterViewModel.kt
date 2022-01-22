package com.nims.bookmark.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nims.bookmark.NotNullMutableLiveData
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path
import java.util.*

class RegisterViewModel(private val repository: RepositoryImpl): ViewModel() {
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
        val context = v.context
        if (context is RegisterActivity) {
            val intent = Intent()
            intent.putExtra(MainActivity.REGISTER_DATA_KEY, RegisterTabType.Path)
            context.setResult(RESULT_OK, intent)
            context.finish()
        }
    }

    fun createFolder(v: View, title: String) {
        val date = Calendar.getInstance().time.time
        val folder = Folder(
            title = title,
            date = date,
            lastUpdate = date
        )
        repository.insertFolder(folder)
        val context = v.context
        if (context is RegisterActivity) {
            val intent = Intent()
            intent.putExtra(MainActivity.REGISTER_DATA_KEY, RegisterTabType.Folder)
            context.setResult(RESULT_OK, intent)
            context.finish()
        }
    }

    fun fetchFolders() {
        _folders.postValue(repository.getFolders())
    }
}