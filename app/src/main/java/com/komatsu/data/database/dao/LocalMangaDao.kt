package com.komatsu.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.komatsu.data.database.entities.LocalMangaEntity
import com.komatsu.data.database.entities.MangaWithCollections
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalMangaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManga(manga: LocalMangaEntity)

    @Update
    suspend fun updateManga(manga: LocalMangaEntity)

    @Query("SELECT * FROM local_manga WHERE mangaId = :mangaId")
    suspend fun getMangaById(mangaId: String): LocalMangaEntity?

    @Query("DELETE FROM local_manga WHERE mangaId = :mangaId")
    suspend fun deleteManga(mangaId: String)

    @Query("SELECT * FROM local_manga")
    suspend fun getAllMangas(): List<LocalMangaEntity>

    @Transaction
    @Query("SELECT * FROM local_manga")
    fun getMangasWithCollections(): List<MangaWithCollections>

    @Transaction
    @Query("SELECT * FROM local_manga")
    fun getMangasWithCollectionsLive(): Flow<List<MangaWithCollections>>

    @Transaction
    @Query("SELECT * FROM local_manga WHERE mangaId = :mangaId")
suspend fun getMangaWithCollectionsById(mangaId: String): MangaWithCollections?

}

