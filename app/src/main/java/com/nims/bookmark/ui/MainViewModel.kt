package com.nims.bookmark.ui

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    fun openRegister(v: View) {
        val context = v.context
        val intent = Intent(context, RegisterActivity::class.java)
        context.startActivity(intent)
    }
}