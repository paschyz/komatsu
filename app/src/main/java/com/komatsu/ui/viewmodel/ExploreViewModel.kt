package com.komatsu.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.komatsu.data.repository.LocalMangaRepository
import com.komatsu.data.repository.MangaCollectionRepository

class ExploreViewModel(mangaCollectionRepository: MangaCollectionRepository,localMangaRepository: LocalMangaRepository) : ViewModel() {
    val allMangaWithCollections = localMangaRepository.getMangasWithCollectionsLive().asLiveData()
}
