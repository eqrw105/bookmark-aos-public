package com.nims.bookmark.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingDialogFragment
import com.nims.bookmark.databinding.FragmentCreateFolderBinding
import com.nims.bookmark.room.Folder
import com.nims.bookmark.ui.main.OnCreateFolderClickListener
import java.util.*

class ARTCreateFolderDialogFragment : BindingDialogFragment<FragmentCreateFolderBinding>(),
    View.OnClickListener {

    override fun getLayoutResId(): Int = R.layout.fragment_create_folder
    private var listener: OnCreateFolderClickListener? = null

    companion object {
        const val TAG = "ARTCreateFolderDialogFragment"

        @JvmStatic
        fun newInstance(listener: OnCreateFolderClickListener) =
            ARTCreateFolderDialogFragment().apply {
                isCancelable = false
                this.listener = listener
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.done.setOnClickListener(this)
        binding.cancel.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.done -> listener?.onCreateFolder(createFolder())
        }
        dismiss()
    }

    private fun createFolder(): Folder {
        val title = binding.title.text.toString()
        val date = Calendar.getInstance().time.time
        return Folder(
            title = title,
            date = date,
            lastUpdate = date
        )
    }
}