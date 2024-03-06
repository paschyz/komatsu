package com.example.komatsu.di.modules

import com.example.komatsu.data.repository.MangaCollectionRepository
import com.example.komatsu.data.repository.MangaRepository
import com.example.komatsu.ui.viewmodel.MangaDetailsViewModel
import com.example.komatsu.ui.viewmodel.MangaListViewModel
import com.example.komatsu.ui.viewmodel.PageViewModel
import com.example.komatsu.ui.viewmodel.ExploreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single { MangaRepository(get()) }
    single { MangaCollectionRepository(get()) }

    viewModel { MangaDetailsViewModel(get()) }
    viewModel { MangaListViewModel(get()) }
    viewModel { PageViewModel(get()) }
    viewModel { ExploreViewModel(get()) }
}

