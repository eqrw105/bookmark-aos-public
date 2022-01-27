package com.nims.bookmark.library

import android.content.Context
import androidx.core.content.edit
import com.nims.bookmark.core.BMApplication

object PrefUtil {
    private const val SELECTED_FOLDER_KEY: String = "SelectedFolderKey"
    private const val BROWSER_MODE_KEY: String = "BrowserModeKey"
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

    var browserMode: Int = 0
        get() {
            return pref.getInt(BROWSER_MODE_KEY, 0)
        }
        set(value) {
            field = value
            pref.edit { this.putInt(BROWSER_MODE_KEY, value).commit() }
        }
}