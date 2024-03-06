package com.example.komatsu.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.komatsu.data.database.entities.MangaCollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaCollectionDao {
    @Query("SELECT * FROM manga_collection")
    fun getAll(): Flow<List<MangaCollectionEntity>>

    @Query("SELECT * FROM manga_collection WHERE id = :id")
    fun getById(id: String): Flow<MangaCollectionEntity>

    @Query("DELETE FROM manga_collection WHERE id = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM manga_collection")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mangaCollectionEntity: MangaCollectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(mangaCollectionEntities: List<MangaCollectionEntity>)

    @Query("SELECT COUNT(*) FROM manga_collection")
    fun count(): Int
}