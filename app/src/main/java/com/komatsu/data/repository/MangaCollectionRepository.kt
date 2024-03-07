package com.komatsu.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.komatsu.data.database.dao.MangaCollectionDao
import com.komatsu.data.database.entities.MangaCollectionEntity
import com.komatsu.data.models.MangaCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MangaCollectionRepository(private val mangaCollectionDao: MangaCollectionDao) {
    val allMangaCollections: Flow<List<MangaCollection>> = mangaCollectionDao.getAllLive().map {
        it.map(MangaCollectionEntity::toMangaCollection)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(mangaCollections: List<MangaCollection>) {
        mangaCollectionDao.insertAll(mangaCollections.map(MangaCollectionEntity::fromMangaCollection))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(mangaCollection: MangaCollection) {
        mangaCollectionDao.insert(MangaCollectionEntity.fromMangaCollection(mangaCollection))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getById(id: String): MangaCollection? {
        return mangaCollectionDao.getById(id)?.toMangaCollection()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAll(): List<MangaCollection> {
        return mangaCollectionDao.getAll().map(MangaCollectionEntity::toMangaCollection)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteById(id: String) {
        mangaCollectionDao.deleteById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        mangaCollectionDao.deleteAll()
    }
}