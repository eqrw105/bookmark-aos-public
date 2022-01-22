package com.nims.bookmark.di

import androidx.room.Room
import com.nims.bookmark.room.BMDatabase
import com.nims.bookmark.ui.RepositoryImpl
import org.koin.dsl.module

val apiModule = module {
    single {
        Room.databaseBuilder(
            get(),
            BMDatabase::class.java, "BMDatabase.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        get<BMDatabase>().pathDao()
    }

    single {
        get<BMDatabase>().folderDao()
    }

    single { RepositoryImpl(get(), get()) }
}