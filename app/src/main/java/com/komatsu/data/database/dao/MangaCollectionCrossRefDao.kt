package com.komatsu.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.komatsu.data.database.relations.MangaCollectionCrossRef

@Dao
interface MangaCollectionCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(crossRef: MangaCollectionCrossRef)

    @Delete
    suspend fun removeCrossRef(crossRef: MangaCollectionCrossRef)

}