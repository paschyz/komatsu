package com.example.komatsu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.komatsu.data.database.entities.LocalMangaEntity
import com.example.komatsu.data.database.entities.MangaWithCollections
import com.example.komatsu.data.models.MangaCollection
import com.example.komatsu.data.repository.LocalMangaRepository
import com.example.komatsu.data.repository.MangaCollectionRepository
import com.example.komatsu.data.repository.MangaRepository
import com.example.komatsu.models.Manga
import kotlinx.coroutines.launch
import kotlin.math.min

class MangaListViewModel(
    private val mangaRepository: MangaRepository,
    mangaCollectionRepository: MangaCollectionRepository,
    private val localMangaRepository: LocalMangaRepository,
) : ViewModel() {

    private val limit = 20
    private var offset = MutableLiveData(0)

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _mangaIds = MutableLiveData<List<String>?>()
    private val _mangas = MutableLiveData<List<Manga>>(emptyList())
    val mangas: LiveData<List<Manga>> get() = _mangas

    // LiveData for manga collections directly observed from the repository
    val mangaCollections: LiveData<List<MangaCollection>> =
        mangaCollectionRepository.allMangaCollections.asLiveData()
    val mangasWithCollections: LiveData<List<MangaWithCollections>> =
        localMangaRepository.allMangasWithCollections.asLiveData()


    private fun appendMangas(newMangas: List<Manga>) {
        val currentMangas = _mangas.value ?: emptyList()
        _mangas.value = currentMangas + newMangas
    }
    fun getMangas(ids: List<String>? = null) {
        viewModelScope.launch {
            try {
                val mangas =
                    mangaRepository.getMangas(
                        ids = ids,
                        includes = listOf("cover_art"),
                        limit = limit,
                        offset = offset.value ?: 0
                    )
                if (mangas.isNullOrEmpty()) {
                    _error.value = "No mangas found"
                    Log.e("MangaListViewModel", "No mangas found")
                    return@launch
                }
                appendMangas(mangas)
                offset.value = (offset.value ?: 0) + mangas.size
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("MangaListViewModel", e.message ?: "Error")
            }
        }
    }

    fun searchMangas(query: String) {
        viewModelScope.launch {
            try {
                val mangas = mangaRepository.searchMangas(query)
                _mangas.postValue(mangas ?: emptyList())
            } catch (e: Exception) {
                _error.value = "Error searching mangas: ${e.message}"
            }
        }
    }

    fun loadMoreMangas(ids: List<String>? = null) {
        if (ids != null) {
            val start = min(offset.value ?: 0, ids.size)
            val end = min(start + limit, ids.size)

            if (start >= ids.size) {
                _error.value = "No more mangas to load"
                return
            }

            val nextIds = ids.subList(start, end)
            _mangaIds.value = nextIds
            offset.value = end
        } else {
            val currentOffset = offset.value ?: 0
            offset.value = currentOffset + limit

            _mangaIds.value = null
        }

        getMangas(_mangaIds.value)
    }

    fun addMangaToCollection(mangaId: String, collectionId: String) {
        viewModelScope.launch {
            try {
                localMangaRepository.insertManga(LocalMangaEntity(mangaId))
                localMangaRepository.insertMangaInCollection(mangaId, collectionId)
            } catch (e: Exception) {
                _error.value = "Error adding manga to collection: ${e.message}"
            }
        }
    }

}
