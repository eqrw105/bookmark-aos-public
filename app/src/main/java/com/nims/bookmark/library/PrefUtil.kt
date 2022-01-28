package com.nims.bookmark.library

import android.content.Context
import androidx.core.content.edit
import com.nims.bookmark.core.BMApplication

enum class BrowserModeType(val mode: Int) {
    Browser(0), WebView(1)
}

object PrefUtil {
    private const val SELECTED_FOLDER_KEY: String = "SelectedFolderKey"
    private const val BROWSER_MODE_KEY: String = "BrowserModeKey"
    private val context = BMApplication.context
    private val pref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    const val defaultFolderId: Int = 1

    var selectedFolderId: Int = defaultFolderId
        get() {
            return pref.getInt(SELECTED_FOLDER_KEY, defaultFolderId)
        }
        set(value) {
            field = value
            pref.edit { this.putInt(SELECTED_FOLDER_KEY, value).commit() }
        }

    var browserMode: Int = BrowserModeType.Browser.mode
        get() {
            return pref.getInt(BROWSER_MODE_KEY, BrowserModeType.Browser.mode)
        }
        set(value) {
            field = value
            pref.edit { this.putInt(BROWSER_MODE_KEY, value).commit() }
        }
}