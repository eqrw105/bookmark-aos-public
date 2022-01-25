package com.nims.bookmark.ui.register

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingActivity
import com.nims.bookmark.databinding.ActivityRegisterBinding
import com.nims.bookmark.ext.addBack
import com.nims.bookmark.ext.replaceTitle
import com.nims.bookmark.ext.setupActionBar
import org.koin.androidx.viewmodel.ext.android.getViewModel

enum class RegisterTabType(val position: Int) {
    Path(0), Folder(1)
}

class RegisterActivity : BindingActivity<ActivityRegisterBinding>() {

    private val registerAdapter: RegisterAdapter = RegisterAdapter(this)

    companion object {
        const val CURRENT_ITEM_KEY: String = "CurrentItemKey"
    }

    override fun getLayoutResId(): Int = R.layout.activity_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = getViewModel()
        setupActionBar(R.id.toolbar)
        replaceTitle(R.string.register_title)
        addBack()

        binding.viewPager.isUserInputEnabled = false
        registerAdapter.items =
            listOf(RegisterPathFragment.newInstance(), RegisterFolderFragment.newInstance())
        binding.viewPager.offscreenPageLimit = registerAdapter.itemCount
        binding.viewPager.adapter = registerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                RegisterTabType.Path.position -> tab.text = getString(R.string.register_path)
                RegisterTabType.Folder.position -> tab.text = getString(R.string.register_folder)
            }
        }.attach()

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val currentItem: Int = savedInstanceState.getInt(CURRENT_ITEM_KEY)
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(currentItem))
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_ITEM_KEY, binding.tabLayout.selectedTabPosition)
        super.onSaveInstanceState(outState)
    }
}