package com.example.komatsu.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.komatsu.data.database.dao.MangaCollectionDao

class PageViewModel(private val mangaCollectionDao: MangaCollectionDao) : ViewModel() {
    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> =
            _index.map {
                "Hello world from section: $it"
            }

    // Use the repository for data operations
    val allMangaCollections = mangaCollectionDao.getAll().asLiveData()

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun getIndex(): Int? {
        return _index.value
    }
}
