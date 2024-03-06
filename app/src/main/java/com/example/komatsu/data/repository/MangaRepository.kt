package com.example.komatsu.data.repository

import com.example.komatsu.domain.models.Manga
import com.example.komatsu.data.api.ApiService
import com.example.komatsu.domain.models.ChapterVolume

class MangaRepository(private val mangaApi: ApiService) {

    companion object {
        private const val LIMIT = 10
        private const val OFFSET = 0
        private val INCLUDES = listOf("cover_art")
        private val CONTENT_RATING = listOf("safe")
    }

    suspend fun getMangas(
        limit: Int? = LIMIT,
        offset: Int? = OFFSET,
        title: String? = null,
        authorOrArtist: String? = null,
        authors: List<String>? = null,
        artists: List<String>? = null,
        year: String? = null,
        includedTags: List<String>? = null,
        includedTagsMode: String? = "AND",
        excludedTags: List<String>? = null,
        excludedTagsMode: String? = "OR",
        status: List<String>? = null,
        originalLanguage: List<String>? = null,
        excludedOriginalLanguage: List<String>? = null,
        availableTranslatedLanguage: List<String>? = null,
        publicationDemographic: List<String>? = null,
        ids: List<String>? = null,
        contentRating: List<String>? = CONTENT_RATING,
        createdAtSince: String? = null,
        updatedAtSince: String? = null,
        order: Map<String, String> = emptyMap(),
        includes: List<String>? = INCLUDES,
        hasAvailableChapters: String? = null,
        group: String? = null,
    ): List<Manga>? {
        val response = mangaApi.getMangas(
            limit,
            offset,
            title,
            authorOrArtist,
            authors,
            artists,
            year,
            includedTags,
            includedTagsMode,
            excludedTags,
            excludedTagsMode,
            status,
            originalLanguage,
            excludedOriginalLanguage,
            availableTranslatedLanguage,
            publicationDemographic,
            ids,
            contentRating,
            createdAtSince,
            updatedAtSince,
            order,
            includes,
            hasAvailableChapters,
            group
        )
        if (response.isSuccessful) {
            return response.body()?.data
        }
        return null
    }

    suspend fun searchMangas(query: String): List<Manga>? {
        return getMangas(title = query)
    }

    suspend fun getManga(id: String, includes: List<String>? = INCLUDES): Manga? {
        val response = mangaApi.getManga(id, includes)
        if (response.isSuccessful) {
            return response.body()?.data
        }
        return null
    }

    suspend fun getVolumes(id: String, includes: List<String>? = listOf("en")): List<ChapterVolume>? {
        val response = mangaApi.getMangaWithVolumesAndChapters(id, includes)
        if (response.isSuccessful) {
            val volumesMap = response.body()?.volumes
            return volumesMap?.values?.toList()
        }
        return null
    }
}
