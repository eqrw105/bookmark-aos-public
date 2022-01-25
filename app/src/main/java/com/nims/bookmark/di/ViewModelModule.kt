package com.nims.bookmark.di

import com.nims.bookmark.ui.main.MainViewModel
import com.nims.bookmark.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}