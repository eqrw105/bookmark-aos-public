package com.nims.bookmark.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingDialogFragment
import com.nims.bookmark.databinding.FragmentProgressBarBinding

class ARTProgressDialogFragment : BindingDialogFragment<FragmentProgressBarBinding>() {
    companion object {
        const val TAG = "ARTProgressDialogFragment"

        @JvmStatic
        fun newInstance() = ARTProgressDialogFragment().apply {
            isCancelable = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun getLayoutResId(): Int = R.layout.fragment_progress_bar
}