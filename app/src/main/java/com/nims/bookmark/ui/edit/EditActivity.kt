package com.nims.bookmark.ui.edit

import android.os.Bundle
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingActivity
import com.nims.bookmark.databinding.ActivityEditBinding
import com.nims.bookmark.ext.addBack
import com.nims.bookmark.ext.replaceTitle
import com.nims.bookmark.ext.setupActionBar
import com.nims.bookmark.library.BrowserModeType
import com.nims.bookmark.library.PrefUtil
import org.koin.androidx.viewmodel.ext.android.getViewModel

class EditActivity : BindingActivity<ActivityEditBinding>() {

    override fun getLayoutResId(): Int = R.layout.activity_edit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = getViewModel()
        setupActionBar(R.id.toolbar)
        replaceTitle(R.string.edit_title)
        addBack()
        binding.viewModel?.fetchFolders()

        when (PrefUtil.browserMode) {
            BrowserModeType.Browser.mode -> binding.browserModeBrowser.isChecked = true
            BrowserModeType.WebView.mode -> binding.browserModeView.isChecked = true
        }
    }
}