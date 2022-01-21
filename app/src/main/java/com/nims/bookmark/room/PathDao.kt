package com.nims.bookmark.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PathDao {
    @Query("SELECT * FROM Path")
    fun getAll(): List<Path>

    @Insert
    fun insertAll(vararg paths: Path)

    @Insert
    fun insert(path: Path)

    @Delete
    fun delete(path: Path)
}