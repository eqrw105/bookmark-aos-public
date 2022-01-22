package com.nims.bookmark.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PathDao {
    @Query("SELECT * FROM Path ORDER BY Path.date DESC")
    fun getAll(): List<Path>

    @Query("SELECT * FROM Path WHERE Path.folderId == :folderId  ORDER BY Path.date DESC")
    fun getAll(folderId: Int): List<Path>

    @Insert
    fun insertAll(vararg paths: Path)

    @Insert
    fun insert(path: Path)

    @Delete
    fun delete(path: Path)
}