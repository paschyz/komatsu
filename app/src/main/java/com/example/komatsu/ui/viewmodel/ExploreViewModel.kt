package com.example.komatsu.ui.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.komatsu.data.database.dao.MangaCollectionDao
import com.example.komatsu.domain.models.Manga
import kotlinx.coroutines.launch

class ExploreViewModel(private val mangaCollectionDao: MangaCollectionDao) : ViewModel() {
    val allMangaCollections = mangaCollectionDao.getAll().asLiveData()
}
