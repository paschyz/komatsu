package com.komatsu.data.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.komatsu.data.database.relations.MangaCollectionCrossRef

data class MangaWithCollections(
    @Embedded val manga: LocalMangaEntity,
    @Relation(
        parentColumn = "mangaId",
        entityColumn = "collectionId",
        associateBy = Junction(MangaCollectionCrossRef::class)
    )
    val collections: List<MangaCollectionEntity>,
)
