package com.nims.bookmark.ui

import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.FolderDao
import com.nims.bookmark.room.Path
import com.nims.bookmark.room.PathDao

interface RegisterRepository {
    fun insertPath(path: Path)
    fun insertFolder(folder: Folder)
    fun getFolders(): List<Folder>
}

class RegisterRepositoryImpl(private val pathDao: PathDao, private val folderDao: FolderDao): RegisterRepository {
    override fun insertPath(path: Path) = pathDao.insert(path)

    override fun insertFolder(folder: Folder) = folderDao.insert(folder)

    override fun getFolders() = folderDao.getAll()

}