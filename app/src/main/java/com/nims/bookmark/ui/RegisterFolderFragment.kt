package com.nims.bookmark.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingFragment
import com.nims.bookmark.databinding.FragmentRegisterFolderBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RegisterFolderFragment : BindingFragment<FragmentRegisterFolderBinding>() {

    companion object {
        const val TITLE_KEY: String = "TitleKey"

        fun newInstance(): RegisterFolderFragment {
            val args = Bundle()
            val fragment = RegisterFolderFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_register_folder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = getViewModel()

        if (savedInstanceState != null) {
            val title = savedInstanceState.getString(RegisterPathFragment.TITLE_KEY)
            binding.title.setText(title)
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val title = binding.title.text.toString()
        outState.putString(TITLE_KEY, title)
        super.onSaveInstanceState(outState)
    }
}