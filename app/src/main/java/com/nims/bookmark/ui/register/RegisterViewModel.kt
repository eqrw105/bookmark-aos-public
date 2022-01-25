package com.nims.bookmark.ui.register

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.nims.bookmark.R
import com.nims.bookmark.library.NotNullMutableLiveData
import com.nims.bookmark.repository.RepositoryImpl
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path
import com.nims.bookmark.ui.main.MainActivity
import java.util.*

class RegisterViewModel(private val repository: RepositoryImpl) : ViewModel() {
    private val _folders: NotNullMutableLiveData<List<Folder>> =
        NotNullMutableLiveData(arrayListOf())
    val folders: LiveData<List<Folder>> get() = _folders

    fun createPath(v: View, title: String, url: String, folder: Any?) {
        val context = v.context
        if (folder == null || folder !is Folder) {
            Snackbar.make(
                v,
                context.getString(R.string.register_path_not_find_folder),
                Snackbar.LENGTH_SHORT
            ).show()
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