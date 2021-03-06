package com.nims.bookmark.di

import com.nims.bookmark.ui.edit.EditViewModel
import com.nims.bookmark.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { EditViewModel(get()) }
}