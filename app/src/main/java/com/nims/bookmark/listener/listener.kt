package com.nims.bookmark.listener

import com.nims.bookmark.room.Path

interface OnPathClickListener {
    fun onPathClick(path: Path)
    fun onPathDelete(path: Path)
    fun onPathShare(path: Path)
}
