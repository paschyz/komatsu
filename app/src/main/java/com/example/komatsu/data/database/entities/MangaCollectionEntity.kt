package com.example.komatsu.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.komatsu.data.models.MangaId
import java.util.UUID


@Entity(tableName = "manga_collection", primaryKeys = ["id"])
data class MangaCollectionEntity(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val editable: Boolean = true,
    val deletable: Boolean = true,
    val mangas: List<MangaId> = emptyList(),
) {

    companion object {

        fun toMangaCollection(mangaCollectionEntity: MangaCollectionEntity) =
            com.example.komatsu.data.models.MangaCollection(
                id = mangaCollectionEntity.id,
                name = mangaCollectionEntity.name,
                editable = mangaCollectionEntity.editable,
                deletable = mangaCollectionEntity.deletable,
                mangas = mangaCollectionEntity.mangas
            )

        fun fromMangaCollection(mangaCollection: com.example.komatsu.data.models.MangaCollection) =
            MangaCollectionEntity(
                id = mangaCollection.id,
                name = mangaCollection.name,
                editable = mangaCollection.editable,
                deletable = mangaCollection.deletable,
                mangas = mangaCollection.mangas
            )
    }
}
