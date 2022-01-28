package com.nims.bookmark.ui.edit

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nims.bookmark.R
import com.nims.bookmark.library.BrowserModeType
import com.nims.bookmark.library.NotNullMutableLiveData
import com.nims.bookmark.library.PrefUtil
import com.nims.bookmark.repository.RepositoryImpl
import com.nims.bookmark.room.Folder

class EditViewModel(private val repository: RepositoryImpl) : ViewModel() {
    private val _folders: NotNullMutableLiveData<List<Folder>> =
        NotNullMutableLiveData(arrayListOf())
    val folders: LiveData<List<Folder>> get() = _folders

    fun fetchFolders() {
        _folders.postValue(repository.getFolders())
    }

    fun updateFolder(folder: Folder) {
        repository.updateFolder(folder)
    }

    fun deleteFolder(folder: Folder) {
        repository.deleteFolder(folder.id)
    }

    fun setBrowserMode(v: View, isChecked: Boolean) {
        if (!isChecked) {
            return
        }
        when (v.id) {
            R.id.browserModeBrowser -> {
                PrefUtil.browserMode = BrowserModeType.Browser.mode
            }
            R.id.browserModeView -> {
                PrefUtil.browserMode = BrowserModeType.WebView.mode
            }
        }
    }
}