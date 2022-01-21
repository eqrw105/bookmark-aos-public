package com.nims.bookmark.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.nims.bookmark.room.Folder

class FolderAdapter(context: Context, private val layout: Int, items: List<Folder>): ArrayAdapter<Folder>(context, layout, items) {
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        val textView = (view as? TextView)
        textView?.text = getItem(position)?.title
        return view
    }
}