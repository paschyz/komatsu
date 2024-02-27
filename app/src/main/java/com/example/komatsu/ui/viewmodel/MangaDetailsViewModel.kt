package com.example.komatsu.ui.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.komatsu.data.repository.MangaRepository
import com.example.komatsu.domain.models.Manga
import kotlinx.coroutines.launch

class MangaDetailsViewModel(private val mangaRepository: MangaRepository) : ViewModel() {
    private val _manga = MutableLiveData<Manga>()
    val manga: LiveData<Manga> = _manga

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

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