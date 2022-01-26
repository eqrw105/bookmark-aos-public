package com.nims.bookmark.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.nims.bookmark.BuildConfig
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingActivity
import com.nims.bookmark.databinding.ActivityMainBinding
import com.nims.bookmark.ext.replaceTitle
import com.nims.bookmark.ext.setupActionBar
import com.nims.bookmark.library.PrefUtil
import com.nims.bookmark.ui.register.RegisterActivity
import com.nims.bookmark.ui.register.RegisterPathFragment
import com.nims.bookmark.ui.register.RegisterTabType
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BindingActivity<ActivityMainBinding>() {

    companion object {
        const val REGISTER_DATA_KEY: String = "RegisterDataKey"
    }

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        binding.adView.loadAd(AdRequest.Builder().build())

        binding.lifecycleOwner = this
        binding.viewModel = getViewModel()
        setupActionBar(R.id.toolbar)
        replaceTitle(R.string.app_name)
        savedInstanceState ?: run {
            binding.viewModel?.fetchFolders()
        }
        binding.tabLayout.removeOnTabSelectedListener(folderSelectedListener)
        binding.tabLayout.addOnTabSelectedListener(folderSelectedListener)

        if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            PrefUtil.selectedFolderId = 0
            val sendText = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
            openRegister(sendText)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.help -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.app_name))
                    setMessage(getString(R.string.app_tutorial))
                    setPositiveButton(getString(R.string.common_done), null)
                }
                    .create()
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val folderSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val folderId = tab?.tag
            (folderId as? Int)?.run {
                binding.viewModel?.fetchPaths(folderId)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    fun openRegister(sendText: String? = null) {
        saveSelectedFolderId()
        val intent = Intent(this, RegisterActivity::class.java)
        sendText?.run {
            intent.putExtra(RegisterPathFragment.URL_KEY, this)
        }
        registerCallback.launch(intent)
    }

    private val registerCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK || it.data == null) {
                return@registerForActivityResult
            }
            val registerData = it.data!!.getSerializableExtra(REGISTER_DATA_KEY) as? RegisterTabType
            registerData?.run {
                when (this) {
                    RegisterTabType.Path -> {
                        refreshPaths()
                        Snackbar.make(
                            binding.root,
                            getString(R.string.register_path_success),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    RegisterTabType.Folder -> {
                        refreshFolders()
                        Snackbar.make(
                            binding.root,
                            getString(R.string.register_folder_success),
                            Snackbar.LENGTH_SHORT
                        ).show()
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

    override fun onDestroy() {
        saveSelectedFolderId()
        super.onDestroy()
    }

    private fun saveSelectedFolderId() {
        val selectedPosition = binding.tabLayout.selectedTabPosition
        val selectedFolderId = binding.tabLayout.getTabAt(selectedPosition)?.tag
        (selectedFolderId as? Int)?.run { PrefUtil.selectedFolderId = this }
    }
}