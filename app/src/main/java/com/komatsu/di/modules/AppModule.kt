package com.komatsu.di.modules

import com.komatsu.data.repository.LocalMangaRepository
import com.komatsu.data.repository.MangaCollectionRepository
import com.komatsu.data.repository.MangaRepository
import com.komatsu.ui.viewmodel.MangaDetailsViewModel
import com.komatsu.ui.viewmodel.MangaListViewModel
import com.komatsu.ui.viewmodel.ExploreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single { MangaRepository(get()) }
    single { MangaCollectionRepository(get()) }
    single { LocalMangaRepository(get(), get(), get()) }

    viewModel { MangaDetailsViewModel(get()) }
    viewModel { MangaListViewModel(get(), get(), get()) }
    viewModel { ExploreViewModel(get(), get()) }
}

