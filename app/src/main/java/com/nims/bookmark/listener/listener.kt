package com.nims.bookmark.listener

import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.Path
import com.nims.bookmark.ui.dialog.CreatePathFailedType

interface OnPathClickListener {
    fun onPathClick(path: Path)
    fun onPathDelete(path: Path)
    fun onPathShare(path: Path)
}

interface OnCreateFolderClickListener {
    fun onCreateFolder(folder: Folder)
}

interface OnCreatePathClickListener {
    fun onCreatePath(path: Path)
    fun onCreatePathFailed(createPathFailedType: CreatePathFailedType)
}

interface OnEditFolderClickListener {
    fun onFolderClick(folder: Folder)
    fun onFolderDelete(folder: Folder)
    fun onFolderMove(fromItem: Folder, toItem: Folder)
}

interface OnUpdateFolderClickListener {
    fun onUpdateFolder(folder: Folder)
}