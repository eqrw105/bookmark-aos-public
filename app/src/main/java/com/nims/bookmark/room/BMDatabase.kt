package com.nims.bookmark.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Path::class, Folder::class], version = 2)
abstract class BMDatabase : RoomDatabase() {
    abstract fun pathDao(): PathDao
    abstract fun folderDao(): FolderDao
}