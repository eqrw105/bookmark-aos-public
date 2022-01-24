package com.nims.bookmark.room

import androidx.room.*

@Dao
interface PathDao {
    @Query("SELECT * FROM Path ORDER BY Path.lastUpdate DESC")
    fun getAll(): List<Path>

    @Query("SELECT * FROM Path WHERE Path.folderId == :folderId  ORDER BY Path.lastUpdate DESC")
    fun getAll(folderId: Int): List<Path>

    @Insert
    fun insertAll(vararg paths: Path)

    @Insert
    fun insert(path: Path)

    @Update
    fun update(path: Path)

    @Delete
    fun delete(path: Path)

    @Query("DELETE FROM Path WHERE Path.folderId == :folderId")
    fun deleteFolderItemAll(folderId: Int)
}