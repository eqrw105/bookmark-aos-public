package com.nims.bookmark.di

import com.nims.bookmark.ui.MainViewModel
import com.nims.bookmark.ui.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}