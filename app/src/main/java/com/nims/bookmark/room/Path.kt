package com.nims.bookmark.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Path(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "folderId") val folderId: Int,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "lastUpdate") var lastUpdate: Long
) : Serializable