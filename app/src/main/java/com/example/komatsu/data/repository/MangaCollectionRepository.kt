package com.example.komatsu.data.repository

import androidx.annotation.WorkerThread
import com.example.komatsu.data.database.dao.MangaCollectionDao
import com.example.komatsu.data.database.entities.MangaCollectionEntity
import com.example.komatsu.data.models.MangaCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MangaCollectionRepository(private val mangaCollectionDao: MangaCollectionDao) {
    val allMangaCollections: Flow<List<MangaCollection>> = mangaCollectionDao.getAll().map {
        it.map(MangaCollectionEntity::toMangaCollection)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(mangaCollection: MangaCollection) {
        mangaCollectionDao.insert(MangaCollectionEntity.fromMangaCollection(mangaCollection))
    }
}