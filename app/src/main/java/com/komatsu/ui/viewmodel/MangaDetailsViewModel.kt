package com.komatsu.ui.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.komatsu.models.Chapter

import com.komatsu.data.repository.MangaRepository
import com.komatsu.models.Manga
import kotlinx.coroutines.launch

class MangaDetailsViewModel(private val mangaRepository: MangaRepository) : ViewModel() {
    private val _manga = MutableLiveData<Manga>()
    val manga: LiveData<Manga> = _manga

    private val _chapters = MutableLiveData<List<Chapter>>()
    val chapters: LiveData<List<Chapter>> = _chapters

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getChapters(id: String, includes: List<String>? = listOf("en")) {
        viewModelScope.launch {
            try {
                val volumes = mangaRepository.getVolumes(id, includes)
                if (volumes == null) {
                    _error.value = "Chapters not found"
                    Log.e("ViewModel", "Chapters not found")
                    return@launch
                }

                val allChapters = mutableListOf<Chapter>()

                volumes.forEach { volume ->
                    volume.chapters.values.forEach { chapter ->
                        allChapters.add(chapter)
                    }
                }

                if (allChapters.isNotEmpty()) {
                    _chapters.value = allChapters
                }

                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ViewModel", e.message ?: "Error")
            }
        }
    }





    fun getManga(id: String) {
        viewModelScope.launch {
            try {
                val manga = mangaRepository.getManga(
                    id,
                    includes = listOf("cover_art"),
                )
                if (manga == null) {
                    _error.value = "Manga not found"
                    Log.e("MangaListViewModel", "Manga not found")
                    return@launch
                }

                _manga.value = manga!!
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("MangaListViewModel", e.message ?: "Error")
            }
        }
    }
}