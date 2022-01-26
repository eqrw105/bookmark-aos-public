package com.nims.bookmark.ui.edit

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingActivity
import com.nims.bookmark.databinding.ActivityEditBinding
import com.nims.bookmark.ext.addBack
import com.nims.bookmark.ext.replaceTitle
import com.nims.bookmark.ext.setupActionBar
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.help -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.edit_tutorial_title))
                    setMessage(getString(R.string.edit_tutorial))
                    setPositiveButton(getString(R.string.common_done), null)
                }
                    .create()
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}