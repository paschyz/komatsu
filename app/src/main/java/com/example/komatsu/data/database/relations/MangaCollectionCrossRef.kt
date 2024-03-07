package com.example.komatsu.data.database.relations

import androidx.room.Entity

@Entity(
    tableName = "manga_collection_cross_ref",
    primaryKeys = ["mangaId", "collectionId"]
)
data class MangaCollectionCrossRef(
    val mangaId: String,
    val collectionId: String,
)
