package com.nims.bookmark.ui.edit

import android.app.AlertDialog
import android.os.Bundle
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingActivity
import com.nims.bookmark.databinding.ActivityEditBinding
import com.nims.bookmark.ext.addBack
import com.nims.bookmark.ext.replaceTitle
import com.nims.bookmark.ext.setupActionBar
import com.nims.bookmark.library.BrowserModeType
import com.nims.bookmark.library.PrefUtil
import com.nims.bookmark.listener.OnEditFolderClickListener
import com.nims.bookmark.listener.OnUpdateFolderClickListener
import com.nims.bookmark.room.Folder
import com.nims.bookmark.ui.dialog.ARTUpdateFolderDialogFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

class EditActivity : BindingActivity<ActivityEditBinding>(), OnEditFolderClickListener,
    OnUpdateFolderClickListener {

    override fun getLayoutResId(): Int = R.layout.activity_edit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = getViewModel()
        binding.listener = this
        setupActionBar(R.id.toolbar)
        replaceTitle(R.string.edit_title)
        addBack()
        binding.viewModel?.fetchFolders()

        when (PrefUtil.browserMode) {
            BrowserModeType.Browser.mode -> binding.browserModeBrowser.isChecked = true
            BrowserModeType.WebView.mode -> binding.browserModeView.isChecked = true
        }
    }

    override fun onFolderClick(folder: Folder) {
        showUpdateFolder(folder, this)
    }

    override fun onFolderDelete(folder: Folder) {
        if (folder.id == 1) {
            showSnackBar(getString(R.string.edit_default_folder_delete))
            return
        }
        AlertDialog.Builder(this).apply {
            setTitle(folder.title)
            setMessage(context.getString(R.string.main_folder_delete_message))
            setPositiveButton(context.getString(R.string.common_delete)) { _, _ ->
                binding.viewModel?.deleteFolder(folder)
                binding.viewModel?.fetchFolders()
                setResult(RESULT_OK)
            }
            setNegativeButton(context.getString(R.string.common_cancel), null)
            setOnCancelListener(null)
        }
            .create()
            .show()
    }

    override fun onFolderMove(fromItem: Folder, toItem: Folder) {
        val tempLastUpdate = fromItem.lastUpdate
        fromItem.lastUpdate = toItem.lastUpdate
        toItem.lastUpdate = tempLastUpdate
        binding.viewModel?.updateFolder(fromItem)
        binding.viewModel?.updateFolder(toItem)
        setResult(RESULT_OK)
    }

    private fun showUpdateFolder(folder: Folder, listener: OnUpdateFolderClickListener) {
        if (supportFragmentManager.findFragmentByTag(ARTUpdateFolderDialogFragment.TAG) == null) {
            val progressDialog = ARTUpdateFolderDialogFragment.newInstance(folder, listener)
            runOnUiThread {
                progressDialog.show(supportFragmentManager, ARTUpdateFolderDialogFragment.TAG)
            }
        }
    }

    private fun hideUpdateFolder() {
        val progressDialog =
            supportFragmentManager.findFragmentByTag(ARTUpdateFolderDialogFragment.TAG) as? ARTUpdateFolderDialogFragment
        progressDialog?.dismissAllowingStateLoss()
    }

    override fun onUpdateFolder(folder: Folder) {
        binding.viewModel?.updateFolder(folder)
        binding.viewModel?.fetchFolders()
        setResult(RESULT_OK)
        hideUpdateFolder()
    }
}