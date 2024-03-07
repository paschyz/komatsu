package com.example.komatsu.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_manga", primaryKeys = ["mangaId"])
data class LocalMangaEntity(
    val mangaId: String,
    val lastChapterRead: Int = 0,
    val lastPageRead: Int = 0,
)