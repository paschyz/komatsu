package com.example.komatsu.data.repository

import com.example.komatsu.data.database.dao.LocalMangaDao
import com.example.komatsu.data.database.dao.MangaCollectionCrossRefDao
import com.example.komatsu.data.database.dao.MangaCollectionDao
import com.example.komatsu.data.database.entities.LocalMangaEntity
import com.example.komatsu.data.database.entities.MangaCollectionEntity
import com.example.komatsu.data.database.entities.MangaWithCollections
import com.example.komatsu.data.database.relations.MangaCollectionCrossRef
import com.example.komatsu.data.models.MangaCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMangaRepository(
    private val localMangaDao: LocalMangaDao,
    private val mangaCollectionDao: MangaCollectionDao,
    private val mangaCollectionCrossRefDao: MangaCollectionCrossRefDao,
) {

    val allMangasWithCollections: Flow<List<MangaWithCollections>> = localMangaDao.getMangasWithCollectionsLive()

    suspend fun insertMangaAndCollection(
        manga: LocalMangaEntity,
        collection: MangaCollectionEntity,
    ) {
        localMangaDao.insertManga(manga)
        mangaCollectionDao.insert(collection)
        mangaCollectionCrossRefDao.insertCrossRef(
            MangaCollectionCrossRef(
                manga.mangaId,
                collection.collectionId
            )
        )
    }

    suspend fun insertMangaInCollection(
        mangaId: String,
        collectionId: String,
    ) {
        mangaCollectionCrossRefDao.insertCrossRef(
            MangaCollectionCrossRef(
                mangaId,
                collectionId
            )
        )
    }

    suspend fun insertManga(manga: LocalMangaEntity) {
        localMangaDao.insertManga(manga)
    }

    suspend fun removeMangaFromCollection(
        mangaId: String,
        collectionId: String,
    ) {
        mangaCollectionCrossRefDao.removeCrossRef(
            MangaCollectionCrossRef(
                mangaId,
                collectionId
            )
        )
    }

    suspend fun deleteManga(mangaId: String) {
        localMangaDao.deleteManga(mangaId)
    }

    suspend fun getAllMangas(): List<LocalMangaEntity> {
        return localMangaDao.getAllMangas()
    }

    suspend fun getMangaById(mangaId: String): LocalMangaEntity? {
        return localMangaDao.getMangaById(mangaId)
    }

    suspend fun updateManga(manga: LocalMangaEntity) {
        localMangaDao.updateManga(manga)
    }

    fun getMangasWithCollections(): List<MangaWithCollections> {
        return localMangaDao.getMangasWithCollections()
    }

    fun getMangasWithCollectionsLive(): Flow<List<MangaWithCollections>> {
        return localMangaDao.getMangasWithCollectionsLive()
    }

    suspend fun getMangaWithCollectionsById(mangaId: String): MangaWithCollections? {
        return localMangaDao.getMangaWithCollectionsById(mangaId)
    }
}