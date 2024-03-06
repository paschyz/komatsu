package com.example.komatsu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.komatsu.data.repository.MangaRepository
import com.example.komatsu.domain.models.Manga
import kotlinx.coroutines.launch
import kotlin.math.min

class MangaListViewModel(private val mangaRepository: MangaRepository) : ViewModel() {
    private val _mangas = MutableLiveData<List<Manga>>()
    val mangas: LiveData<List<Manga>> = _mangas

    private val limit = 20
    private var offset = 0

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getMangas(ids: List<String>? = null) {
        viewModelScope.launch {
            try {
                val mangas =
                        mangaRepository.getMangas(
                                ids = ids,
                                includes = listOf("cover_art"),
                                limit = limit,
                                offset = offset
                        )
                if (mangas.isNullOrEmpty()) {
                    _error.value = "No mangas found"
                    Log.e("MangaListViewModel", "No mangas found")
                    return@launch
                }
                _mangas.value = mangas!!
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
                if (mangas.isNullOrEmpty()) {
                    _error.value = "No mangas found"
                    Log.e("MangaListViewModel", "No mangas found")
                    return@launch
                }
                _mangas.value = mangas!!
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("MangaListViewModel", e.message ?: "Error")
            }
        }
    }

    fun loadMoreMangas(ids: List<String>? = null) {
        // if ids is not null, take a slice of the list of ids
        var sliceIds: List<String>? = null
        if (ids != null) {
            val start = offset
            val end = min(start + limit, ids.size)

            if (start < ids.size) {
                sliceIds = ids.slice(start until end)
            }
        }

        viewModelScope.launch {
            offset += limit
            try {
                val mangas =
                        mangaRepository.getMangas(
                                includes = listOf("cover_art"),
                                limit = limit,
                                offset = offset,
                                ids = sliceIds
                        )
                if (mangas.isNullOrEmpty()) {
                    _error.value = "No more mangas found"
                    Log.e("MangaListViewModel", "No more mangas found")
                    return@launch
                }
                _mangas.value = _mangas.value?.plus(mangas!!)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("MangaListViewModel", e.message ?: "Error")
            }
        }
    }
}

