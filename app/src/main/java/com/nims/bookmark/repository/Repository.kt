package com.nims.bookmark.repository

import com.nims.bookmark.room.Folder
import com.nims.bookmark.room.FolderDao
import com.nims.bookmark.room.Path
import com.nims.bookmark.room.PathDao

interface Repository {
    fun insertPath(path: Path)
    fun insertFolder(folder: Folder)
    fun getPaths(folderId: Int): List<Path>
    fun getFolders(): List<Folder>
    fun deletePath(path: Path)
    fun updatePath(path: Path)
    fun deleteFolder(folderId: Int)
    fun updateFolder(folder: Folder)
    fun getFoldersWhereDate(): List<Folder>
}

class RepositoryImpl(private val pathDao: PathDao, private val folderDao: FolderDao) : Repository {
    override fun insertPath(path: Path) = pathDao.insert(path)
    override fun insertFolder(folder: Folder) = folderDao.insert(folder)
    override fun getPaths(folderId: Int): List<Path> {
        if (folderId == 1) {
            return pathDao.getAll()
        }
        return pathDao.getAll(folderId)
    }

    override fun getFolders() = folderDao.getAll()
    override fun deletePath(path: Path) = pathDao.delete(path)
    override fun updatePath(path: Path) = pathDao.update(path)
    override fun deleteFolder(folderId: Int) {
        pathDao.deleteFolderItemAll(folderId)
        folderDao.delete(folderId)
    }
    override fun updateFolder(folder: Folder) = folderDao.update(folder)
    override fun getFoldersWhereDate(): List<Folder> = folderDao.getAllWhereDate()

}