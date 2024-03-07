package com.example.komatsu.ui.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.komatsu.data.database.dao.MangaCollectionDao
import com.example.komatsu.data.repository.LocalMangaRepository
import com.example.komatsu.data.repository.MangaCollectionRepository
import com.example.komatsu.domain.models.Manga
import kotlinx.coroutines.launch

class ExploreViewModel(mangaCollectionRepository: MangaCollectionRepository,localMangaRepository: LocalMangaRepository) : ViewModel() {
    val allMangaWithCollections = localMangaRepository.getMangasWithCollectionsLive().asLiveData()
}
