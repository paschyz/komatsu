package com.komatsu.data.models

data class MangaCollection(
    val id: String,
    val name: String,
    val editable: Boolean = true,
    val deletable: Boolean = true,
)

