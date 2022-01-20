package com.nims.bookmark.core

import android.app.Application
import com.nims.bookmark.di.viewModelModule
import org.koin.core.context.startKoin

class BMApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(
                viewModelModule
            ))
        }
    }
}