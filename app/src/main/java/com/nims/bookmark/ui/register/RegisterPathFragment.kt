package com.nims.bookmark.ui.register

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingFragment
import com.nims.bookmark.databinding.FragmentRegisterPathBinding
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RegisterPathFragment : BindingFragment<FragmentRegisterPathBinding>() {

    private var folderAdapter: FolderAdapter =
        FolderAdapter(get(), R.layout.item_common_spinner, arrayListOf())

    companion object {
        const val TITLE_KEY: String = "TitleKey"
        const val URL_KEY: String = "UrlKey"
        const val FOLDER_CURRENT_ITEM_KEY: String = "FolderCurrentItemKey"

        fun newInstance(): RegisterPathFragment {
            val args = Bundle()
            val fragment = RegisterPathFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_register_path

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = getViewModel()
        binding.folder.adapter = folderAdapter
        binding.folder.onItemSelectedListener = folderItemSelectedListener

        binding.viewModel?.fetchFolders()
        binding.viewModel?.folders?.observe(viewLifecycleOwner, {
            folderAdapter.clear()
            folderAdapter.addAll(it.filter { it.id != 1 })
        })

        activity?.intent?.getStringExtra(URL_KEY)?.run {
            binding.url.post { binding.url.setText(this) }
        }
        return binding.root
    }

    private val folderItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            (parent?.getChildAt(0) as? TextView)?.apply {
                text = folderAdapter.getItem(position)?.title.toString()
                setTextColor(Color.BLACK)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        val title = savedInstanceState?.getString(TITLE_KEY) ?: ""
        binding.title.setText(title)

        val url = savedInstanceState?.getString(URL_KEY) ?: ""
        binding.url.setText(url)
        binding.folder.post {
            val currentItem = savedInstanceState?.getInt(FOLDER_CURRENT_ITEM_KEY) ?: 0
            binding.folder.setSelection(currentItem)
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val title = binding.title.text.toString()
        val url = binding.url.text.toString()
        val currentItem = binding.folder.selectedItemPosition
        outState.putString(TITLE_KEY, title)
        outState.putString(URL_KEY, url)
        outState.putInt(FOLDER_CURRENT_ITEM_KEY, currentItem)
        super.onSaveInstanceState(outState)
    }
}