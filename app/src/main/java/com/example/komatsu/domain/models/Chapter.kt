package com.example.komatsu.domain.models

data class Chapter(
    val chapter: String,
    val id: String,
    val others: List<String>,
    val count: Int
)

data class ChapterVolume(
    val volume: String,
    val count: Int,
    val chapters: Map<String, Chapter>
)

data class ChapterResponse(
    val result: String,
    val volumes: Map<String, ChapterVolume>
)