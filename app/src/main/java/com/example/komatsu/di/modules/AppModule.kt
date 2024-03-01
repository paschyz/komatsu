package com.example.komatsu.di.modules

import com.example.komatsu.ui.viewmodel.MangaListViewModel
import com.example.komatsu.data.repository.MangaRepository
import com.example.komatsu.ui.viewmodel.MangaDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single { MangaRepository(get()) }
    viewModel { MangaDetailsViewModel(get()) }
    viewModel { MangaListViewModel(get()) }
}
