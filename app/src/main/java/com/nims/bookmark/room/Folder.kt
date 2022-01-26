package com.nims.bookmark.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "lastUpdate") var lastUpdate: Long
)