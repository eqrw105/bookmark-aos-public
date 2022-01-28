package com.nims.bookmark.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingDialogFragment
import com.nims.bookmark.databinding.FragmentUpdateFolderBinding
import com.nims.bookmark.listener.OnUpdateFolderClickListener
import com.nims.bookmark.room.Folder

class ARTUpdateFolderDialogFragment : BindingDialogFragment<FragmentUpdateFolderBinding>(),
    View.OnClickListener {

    override fun getLayoutResId(): Int = R.layout.fragment_update_folder
    private var listener: OnUpdateFolderClickListener? = null
    private var folder: Folder? = null

    companion object {
        const val TAG = "ARTUpdateFolderDialogFragment"

        @JvmStatic
        fun newInstance(folder: Folder, listener: OnUpdateFolderClickListener) =
            ARTUpdateFolderDialogFragment().apply {
                isCancelable = false
                this.listener = listener
                this.folder = folder
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
        binding.title.setText(folder?.title ?: "")
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.done -> updateFolder()?.run { listener?.onUpdateFolder(this) }
        }
        dismiss()
    }

    private fun updateFolder(): Folder? {
        val title = binding.title.text.toString()
        return folder?.copy(title = title)
    }
}