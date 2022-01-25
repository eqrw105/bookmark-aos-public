package com.nims.bookmark.library

import android.content.Context
import androidx.core.content.edit
import com.nims.bookmark.core.BMApplication

object PrefUtil {
    private const val SELECTED_FOLDER_KEY: String = "SelectedFolderKey"
    private val context = BMApplication.context
    private val pref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    var selectedFolderId: Int = 0
        get() {
            return pref.getInt(SELECTED_FOLDER_KEY, 0)
        }
        set(value) {
            field = value
            pref.edit { this.putInt(SELECTED_FOLDER_KEY, value).commit() }
        }
}