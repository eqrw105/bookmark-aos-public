package com.nims.bookmark.room

import androidx.room.*

@Dao
interface FolderDao {
    @Query("SELECT * FROM Folder ORDER BY Folder.lastUpdate ASC")
    fun getAll(): List<Folder>

    @Query("SELECT * FROM Folder ORDER BY Folder.date ASC")
    fun getAllWhereDate(): List<Folder>

    @Insert
    fun insertAll(vararg folders: Folder)

    @Insert
    fun insert(folder: Folder)

    @Delete
    fun delete(folder: Folder)

    @Query("DELETE FROM Folder WHERE Folder.id == :folderId")
    fun delete(folderId: Int)

    @Update
    fun update(folder: Folder)
}