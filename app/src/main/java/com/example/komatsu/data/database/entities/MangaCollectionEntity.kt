package com.example.komatsu.data.database.entities

import androidx.room.Entity
import com.example.komatsu.data.models.MangaCollection
import com.example.komatsu.data.models.MangaId
import java.util.UUID


@Entity(tableName = "manga_collection", primaryKeys = ["collectionId"])
data class MangaCollectionEntity(
    val collectionId: String = UUID.randomUUID().toString(),
    val name: String,
    val editable: Boolean = true,
    val deletable: Boolean = true,
) {
    fun toMangaCollection(): MangaCollection {
        return MangaCollection(
            id = collectionId,
            name = name,
            editable = editable,
            deletable = deletable,
        )
    }

    companion object {

        fun toMangaCollection(mangaCollectionEntity: MangaCollectionEntity) =
            MangaCollection(
                id = mangaCollectionEntity.collectionId,
                name = mangaCollectionEntity.name,
                editable = mangaCollectionEntity.editable,
                deletable = mangaCollectionEntity.deletable,
            )

        fun fromMangaCollection(mangaCollection: MangaCollection) =
            MangaCollectionEntity(
                collectionId = mangaCollection.id,
                name = mangaCollection.name,
                editable = mangaCollection.editable,
                deletable = mangaCollection.deletable,
            )
    }
}
