package com.nims.bookmark.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingActivity
import com.nims.bookmark.databinding.ActivityMainBinding
import com.nims.bookmark.ext.*
import com.nims.bookmark.library.BrowserModeType
import com.nims.bookmark.library.PrefUtil
import com.nims.bookmark.listener.OnCreateFolderClickListener
import com.nims.bookmark.listener.OnCreatePathClickListener
import com.nims.bookmark.listener.OnPathClickListener
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path
import com.nims.bookmark.ui.detail.DetailActivity
import com.nims.bookmark.ui.dialog.CreatePathFailedType
import com.nims.bookmark.ui.edit.EditActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.*

class MainActivity : BindingActivity<ActivityMainBinding>(), OnCreateFolderClickListener,
    OnCreatePathClickListener,
    OnPathClickListener {

    private var menu: Menu? = null

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        binding.adView.loadAd(AdRequest.Builder().build())
        binding.listener = this
        binding.lifecycleOwner = this
        binding.viewModel = getViewModel()
        setupActionBar(R.id.toolbar)
        replaceTitle(R.string.app_name)
        refreshFolders()
        binding.tabLayout.removeOnTabSelectedListener(folderSelectedListener)
        binding.tabLayout.addOnTabSelectedListener(folderSelectedListener)

        if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            sendCallback()
        }
    }

    private fun sendCallback() {
        val sendText = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""

        val folders = binding.viewModel?.getFolderList()?.filter { it.id != 1 }
        if (folders.isNullOrEmpty()) {
            showSnackBar(getString(R.string.main_create_path_not_find_folder))
            return
        }
        showSelectFolder(folders, sendText)
    }

    private fun showSelectFolder(folders: List<Folder>, sendText: String) {
        val folderTitleList = folders.associateWith { it.title }.values.toTypedArray()
        val folderIdList = folders.associateWith { it.id }.values.toTypedArray()

        AlertDialog.Builder(this).setTitle(getString(R.string.main_share_folder_title))
            .setItems(folderTitleList) { _, i ->
                PrefUtil.selectedFolderId = folderIdList[i]
                findSelectedTab()?.select()
                showCreatePath("", sendText, this)
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun findSelectedTab(): TabLayout.Tab? {
        binding.tabLayout.run {
            val len = tabCount
            for (i in 0 until len) {
                val tab = getTabAt(i)
                if (tab?.tag == PrefUtil.selectedFolderId) {
                    return tab
                }
            }
        }
        return null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> openEdit()
            R.id.createFolder -> showCreateFolder(this)
            R.id.createPath -> showCreatePath("", "", this)
            R.id.help -> showHelp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showHelp() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.app_name))
            setMessage(getString(R.string.app_tutorial))
            setPositiveButton(getString(R.string.common_done), null)
        }
            .create()
            .show()
    }

    private val folderSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tabSelectCallback(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {
            tabSelectCallback(tab)
        }
    }

    private fun tabSelectCallback(tab: TabLayout.Tab?) {
        val tabCount = binding.tabLayout.tabCount
        val folderId = tab?.tag
        if (folderId is Int && tabCount > 0) {
            binding.tabLayout.post {
                PrefUtil.selectedFolderId = folderId
                refreshPaths()
                menu?.findItem(R.id.createPath)?.isVisible = folderId != 1
            }
        }
    }

    private fun refreshPaths() {
        binding.viewModel?.fetchPaths(PrefUtil.selectedFolderId)
    }

    private fun refreshFolders() {
        binding.viewModel?.fetchFolders()
    }

    private val editCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }
            refreshFolders()
        }

    private fun openEdit() {
        val intent = Intent(this, EditActivity::class.java)
        editCallback.launch(intent)
    }

    override fun onCreateFolder(folder: Folder) {
        binding.viewModel?.insertFolder(folder)
        PrefUtil.selectedFolderId = binding.viewModel?.getNewFolderId() ?: PrefUtil.defaultFolderId
        refreshFolders()
        showSnackBar(getString(R.string.main_create_folder_success))
        hideCreateFolder()
    }

    override fun onCreatePath(path: Path) {
        binding.viewModel?.insertPath(path)
        refreshPaths()
        showSnackBar(getString(R.string.main_create_path_success))
        hideCreatePath()
    }

    override fun onCreatePathFailed(createPathFailedType: CreatePathFailedType) {
        when (createPathFailedType) {
            CreatePathFailedType.Url -> showSnackBar(getString(R.string.main_create_path_url_error))
            CreatePathFailedType.Folder -> showSnackBar(getString(R.string.main_create_path_not_find_folder))
        }
        hideCreatePath()
    }

    override fun onPathClick(path: Path) {
        when (PrefUtil.browserMode) {
            BrowserModeType.Browser.mode -> {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = path.url.toUri()
                }.run {
                    startActivity(this)
                }
            }
            BrowserModeType.WebView.mode -> {
                Intent(this, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.PATH_ITEM_KEY, path)
                }.run {
                    startActivity(this)
                }
            }
        }
    }

    override fun onPathDelete(path: Path) {
        AlertDialog.Builder(this).apply {
            setTitle(path.title)
            setMessage(context.getString(R.string.main_path_delete_message))
            setPositiveButton(context.getString(R.string.common_delete)) { _, _ ->
                binding.viewModel?.deletePath(path)
                refreshPaths()
            }
            setNegativeButton(context.getString(R.string.common_cancel), null)
            setOnCancelListener(null)
        }.create().show()
    }

    override fun onPathShare(path: Path) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            val message = path.url
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }
}