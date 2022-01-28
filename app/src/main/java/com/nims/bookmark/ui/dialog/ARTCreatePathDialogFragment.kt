package com.nims.bookmark.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingDialogFragment
import com.nims.bookmark.databinding.FragmentCreatePathBinding
import com.nims.bookmark.library.PrefUtil
import com.nims.bookmark.room.Path
import com.nims.bookmark.ui.main.OnCreatePathClickListener
import java.util.*

enum class CreatePathFailedType {
    Url, Folder
}

class ARTCreatePathDialogFragment : BindingDialogFragment<FragmentCreatePathBinding>(),
    View.OnClickListener {

    override fun getLayoutResId(): Int = R.layout.fragment_create_path
    private var listener: OnCreatePathClickListener? = null
    private var title: String = ""
    private var url: String = ""

    companion object {
        const val TAG = "ARTCreateDialogFragment"

        @JvmStatic
        fun newInstance(
            title: String,
            url: String,
            listener: OnCreatePathClickListener
        ) =
            ARTCreatePathDialogFragment().apply {
                isCancelable = false
                this.listener = listener
                this.title = title
                this.url = url
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.title.setText(title)
        binding.url.setText(url)

        binding.done.setOnClickListener(this)
        binding.cancel.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.done -> createPath()?.run { listener?.onCreatePath(this) }
        }
        dismiss()
    }

    private fun createPath(): Path? {
        val title = binding.title.text.toString()
        val url = binding.url.text.toString()
        val isValidUrl = URLUtil.isValidUrl(url) && url.isNotBlank()
        if (!isValidUrl) {
            listener?.onCreatePathFailed(CreatePathFailedType.Url)
            return null
        }
        val date = Calendar.getInstance().time.time
        val folderId = PrefUtil.selectedFolderId
        if (folderId == 1) {
            listener?.onCreatePathFailed(CreatePathFailedType.Folder)
            return null
        }
        return Path(
            title = title,
            url = url,
            folderId = folderId,
            date = date,
            lastUpdate = date
        )
    }

}