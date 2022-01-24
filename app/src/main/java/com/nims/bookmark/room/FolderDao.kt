package com.nims.bookmark.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FolderDao {
    @Query("SELECT * FROM Folder")
    fun getAll(): List<Folder>

    @Insert
    fun insertAll(vararg folders: Folder)

    @Insert
    fun insert(folder: Folder)

    @Delete
    fun delete(folder: Folder)

    @Query("DELETE FROM Folder WHERE Folder.id == :folderId")
    fun delete(folderId: Int)
}