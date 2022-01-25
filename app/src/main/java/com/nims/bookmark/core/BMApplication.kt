package com.nims.bookmark.core

import android.app.Application
import android.content.Context
import com.nims.bookmark.R
import com.nims.bookmark.di.apiModule
import com.nims.bookmark.di.viewModelModule
import com.nims.bookmark.repository.RepositoryImpl
import com.nims.bookmark.room.Folder
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.*

class BMApplication : Application() {
    companion object {
        lateinit var context: BMApplication
    }
    override fun onCreate() {
        super.onCreate()
        context = this
        startKoin {
            androidContext(this@BMApplication)
            modules(
                listOf(
                    apiModule,
                    viewModelModule
                )
            )
        }
        initFolder()
    }

    /**
     * folder 없을 시 전체 폴더 추가
     */
    private fun initFolder() {
        val repository: RepositoryImpl = get()
        if (repository.getFolders().isNullOrEmpty()) {
            val date = Calendar.getInstance().time.time
            val folder = Folder(
                title = getString(R.string.common_all),
                date = date,
                lastUpdate = date
            )
            repository.insertFolder(folder)
        }
    }
}