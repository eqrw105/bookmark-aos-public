package com.nims.bookmark.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.nims.bookmark.R
import com.nims.bookmark.library.dp2px
import com.nims.bookmark.library.getScreenWidth

abstract class BindingDialogFragment<T : ViewDataBinding> : DialogFragment() {

    @LayoutRes
    abstract fun getLayoutResId(): Int

    protected lateinit var binding: T
        private set

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (getLayoutResId() == R.layout.fragment_progress_bar) {
            return
        }
        dialog?.window?.apply {
            val width = getScreenWidth() - (dp2px(20f).toInt() * 2)
            val height = attributes.height
            setLayout(width, height)
        }
    }
}