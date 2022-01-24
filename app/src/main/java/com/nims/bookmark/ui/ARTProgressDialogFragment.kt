package com.nims.bookmark.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nims.bookmark.R

class ARTProgressDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "ARTProgressDialogFragment"

        @JvmStatic
        fun newInstance() = ARTProgressDialogFragment().apply {
            isCancelable = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_progress_bar, container, false)
    }
}