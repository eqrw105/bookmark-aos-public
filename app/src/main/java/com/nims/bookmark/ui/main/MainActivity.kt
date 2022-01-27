package com.nims.bookmark.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.setPadding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingActivity
import com.nims.bookmark.databinding.ActivityMainBinding
import com.nims.bookmark.ext.replaceTitle
import com.nims.bookmark.ext.setupActionBar
import com.nims.bookmark.library.PrefUtil
import com.nims.bookmark.library.dp2px
import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path
import com.nims.bookmark.ui.edit.EditActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.*

class MainActivity : BindingActivity<ActivityMainBinding>() {

    private var menu: Menu? = null

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
            sendCallback()
        }
    }

    private fun sendCallback() {
        val sendText = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""

        val folderIdList: ArrayList<Int> = arrayListOf()
        val folderList: ArrayList<CharSequence> = arrayListOf()
        binding.viewModel?.getFolderList()?.filter { it.id != 1 }?.withIndex()?.forEach {
            folderIdList.add(it.index, it.value.id)
            folderList.add(it.index, it.value.title)
        }
        AlertDialog.Builder(this).setTitle(getString(R.string.main_share_folder_title))
            .setItems(folderList.toTypedArray()) { _, i ->
                PrefUtil.selectedFolderId = folderIdList[i]
                selectFolder()
                createPath(url = sendText)
            }.create().show()
    }

    private fun createFolder(title: String = "") {
        val editText = EditText(this@MainActivity).apply {
            setText(title)
            hint = getString(R.string.main_create_folder_hint)
        }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp2px(20f).toInt())
            addView(editText)
        }

        val positiveCallback = DialogInterface.OnClickListener { _, _ ->
            val date = Calendar.getInstance().time.time
            val folder = Folder(
                title = (container.getChildAt(0) as? EditText)?.text.toString(),
                date = date,
                lastUpdate = date
            )
            binding.viewModel?.createFolder(folder)
            PrefUtil.selectedFolderId = binding.viewModel?.getNewFolderId() ?: 0
            refreshFolders()
            Snackbar.make(
                binding.root,
                getString(R.string.main_create_folder_success),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.main_create_folder_title))
            setPositiveButton(getString(R.string.common_add), positiveCallback)
            setNegativeButton(getString(R.string.common_cancel), null)
            setView(container)
        }.create().show()
    }

    private fun createPath(title: String = "", url: String = "") {
        val titleEditText = EditText(this@MainActivity).apply {
            setText(title)
            hint = getString(R.string.main_create_path_title_hint)
        }
        val urlEditText = EditText(this@MainActivity).apply {
            setText(url)
            hint = getString(R.string.main_create_path_url_hint)
        }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp2px(20f).toInt())
            addView(titleEditText)
            addView(urlEditText)
        }

        val positiveCallback = DialogInterface.OnClickListener { _, _ ->
            //url 프로토콜 형식 체크
            val title = titleEditText.text.toString()
            val url = urlEditText.text.toString()
            val isValidUrl = URLUtil.isValidUrl(url) && url.isNotBlank()
            if (!isValidUrl) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.main_create_path_url_error),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val date = Calendar.getInstance().time.time
            val folderId = PrefUtil.selectedFolderId
            if (folderId == 1) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.main_create_path_not_find_folder),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val path = Path(
                title = title,
                url = url,
                folderId = folderId,
                date = date,
                lastUpdate = date
            )
            binding.viewModel?.createPath(path)
            refreshPaths()
            Snackbar.make(
                binding.root,
                getString(R.string.main_create_path_success),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.main_create_folder_title))
            setPositiveButton(getString(R.string.common_add), positiveCallback)
            setNegativeButton(getString(R.string.common_cancel), null)
            setView(container)
        }.create().show()
    }

    private fun selectFolder() {
        binding.tabLayout.run {
            val len = tabCount
            for (i in 0 until len) {
                val tab = getTabAt(i)
                if (tab?.tag == PrefUtil.selectedFolderId) {
                    selectTab(tab)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.help -> showHelp()
            R.id.createFolder -> createFolder()
            R.id.createPath -> createPath()
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
            val tabCount = binding.tabLayout.tabCount
            val folderId = tab?.tag
            (folderId as? Int)?.run {
                if (tabCount > 1) {
                    binding.tabLayout.post {
                        PrefUtil.selectedFolderId = folderId
                        refreshPaths()
                        menu?.findItem(R.id.createPath)?.isVisible = folderId != 1
                    }
                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
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

    fun openEdit() {
        val intent = Intent(this, EditActivity::class.java)
        editCallback.launch(intent)
    }
}