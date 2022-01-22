package com.nims.bookmark.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingActivity
import com.nims.bookmark.databinding.ActivityMainBinding
import com.nims.bookmark.ext.replaceTitle
import com.nims.bookmark.ext.setupActionBar
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BindingActivity<ActivityMainBinding>() {

    companion object {
        const val REGISTER_DATA_KEY: String = "RegisterDataKey"
    }

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = getViewModel()
        setupActionBar(R.id.toolbar)
        replaceTitle(R.string.app_name)
        binding.viewModel?.fetchFolders()
        binding.tabLayout.removeOnTabSelectedListener(folderSelectedListener)
        binding.tabLayout.addOnTabSelectedListener(folderSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                Snackbar.make(binding.root, "Setting Pressed", Snackbar.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val folderSelectedListener = object: TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val folderId = tab?.tag
            (folderId as? Int)?.run { binding.viewModel?.fetchPaths(folderId) }
        }
        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    fun openRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        registerCallback.launch(intent)
    }

    private val registerCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != RESULT_OK || it.data == null) {
            return@registerForActivityResult
        }
        val registerData = it.data!!.getSerializableExtra(REGISTER_DATA_KEY) as? RegisterTabType
        registerData?.run {
            when(this) {
                RegisterTabType.Path -> {
                    refreshPaths()
                    Snackbar.make(binding.root, getString(R.string.register_path_success), Snackbar.LENGTH_SHORT).show()
                }
                RegisterTabType.Folder ->  {
                    refreshFolders()
                    Snackbar.make(binding.root, getString(R.string.register_folder_success), Snackbar.LENGTH_SHORT).show()
                }
            }
        } ?: kotlin.run {
            return@registerForActivityResult
        }
    }

    private fun refreshPaths() {
        val selectedPosition = binding.tabLayout.selectedTabPosition
        val folderId = binding.tabLayout.getTabAt(selectedPosition)?.tag
        (folderId as? Int)?.run { binding.viewModel?.fetchPaths(this) }
    }

    private fun refreshFolders() {
        binding.viewModel?.fetchFolders()
    }
}