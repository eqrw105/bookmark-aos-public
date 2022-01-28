package com.nims.bookmark.ui.edit

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.setPadding
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.nims.bookmark.R
import com.nims.bookmark.library.NotNullMutableLiveData
import com.nims.bookmark.library.PrefUtil
import com.nims.bookmark.library.dp2px
import com.nims.bookmark.repository.RepositoryImpl
import com.nims.bookmark.room.Folder

class EditViewModel(private val repository: RepositoryImpl) : ViewModel() {
    private val _folders: NotNullMutableLiveData<List<Folder>> =
        NotNullMutableLiveData(arrayListOf())
    val folders: LiveData<List<Folder>> get() = _folders

    fun fetchFolders() {
        _folders.postValue(repository.getFolders())
    }

    fun updateFolder(v: View, fromItem: Folder, toItem: Folder) {
        val tempLastUpdate = fromItem.lastUpdate
        fromItem.lastUpdate = toItem.lastUpdate
        toItem.lastUpdate = tempLastUpdate
        repository.updateFolder(fromItem)
        repository.updateFolder(toItem)
        putResult(v.context)
    }

    fun deleteFolder(v: View, folder: Folder) {
        val context = v.context
        if (folder.id == 1) {
            Snackbar.make(
                v,
                context.getString(R.string.edit_default_folder_delete),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }
        openAlert(context, folder,
            successCallback = {
                repository.deleteFolder(folder.id)
                fetchFolders()
                putResult(context)
            },
            failedCallback = {}
        )

    }

    private fun openAlert(
        context: Context,
        item: Folder,
        successCallback: () -> Unit,
        failedCallback: () -> Unit
    ) {
        AlertDialog.Builder(context).apply {
            setTitle(item.title)
            setMessage(context.getString(R.string.main_folder_delete_message))
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

    fun updateTitle(v: View, item: Folder) {
        val context = v.context
        val frameLayout = FrameLayout(context)
        frameLayout.setPadding(dp2px(20f).toInt())
        val editText = EditText(v.context)
        editText.setText(item.title)
        editText.hint = context.getString(R.string.edit_folder_update_hint)
        frameLayout.addView(editText)

        AlertDialog.Builder(context).apply {
            setTitle(context.getString(R.string.edit_folder_update_title))
            setView(frameLayout)
            setPositiveButton(context.getString(R.string.common_update)) { _, _ ->
                val updateItem = item.copy(title = editText.text.toString())
                repository.updateFolder(updateItem)
                fetchFolders()
                putResult(context)
            }
            setNegativeButton(context.getString(R.string.common_cancel), null)
        }
            .create()
            .show()
    }

    private fun putResult(context: Context) {
        (context as? EditActivity)?.apply {
            val intent = Intent()
            setResult(RESULT_OK, intent)
        }
    }

    fun setBrowserMode(v: View, isChecked: Boolean) {
        if (!isChecked) {
            return
        }
        when (v.id) {
            R.id.browserModeBrowser -> {
                PrefUtil.browserMode = 0
            }
            R.id.browserModeView -> {
                PrefUtil.browserMode = 1
            }
        }
    }
}