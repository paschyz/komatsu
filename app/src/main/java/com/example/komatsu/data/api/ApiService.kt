package com.example.komatsu.data.api


import com.example.komatsu.domain.models.Manga
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET("/manga")
    suspend fun getMangas(
        @Query("limit") limit: Int? = 10,
        @Query("offset") offset: Int? = 0,
        @Query("title") title: String? = null,
        @Query("authorOrArtist") authorOrArtist: String? = null,
        @Query("authors[]") authors: List<String>? = null,
        @Query("artists[]") artists: List<String>? = null,
        @Query("year") year: String? = null, // Can be an integer or "none"
        @Query("includedTags[]") includedTags: List<String>? = null,
        @Query("includedTagsMode") includedTagsMode: String? = "AND",
        @Query("excludedTags[]") excludedTags: List<String>? = null,
        @Query("excludedTagsMode") excludedTagsMode: String? = "OR",
        @Query("status[]") status: List<String>? = null,
        @Query("originalLanguage[]") originalLanguage: List<String>? = null,
        @Query("excludedOriginalLanguage[]") excludedOriginalLanguage: List<String>? = null,
        @Query("availableTranslatedLanguage[]") availableTranslatedLanguage: List<String>? = null,
        @Query("publicationDemographic[]") publicationDemographic: List<String>? = null,
        @Query("ids[]") ids: List<String>? = null,
        @Query("contentRating[]") contentRating: List<String>? = null,
        @Query("createdAtSince") createdAtSince: String? = null,
        @Query("updatedAtSince") updatedAtSince: String? = null,
        @QueryMap order: Map<String, String>? = null, // For sorting
        @Query("includes[]") includes: List<String>? = null,
        @Query("hasAvailableChapters") hasAvailableChapters: String? = null,
        @Query("group") group: String? = null,
    ): Response<Manga.MangasResponse>

    @GET("/manga/{id}")
    suspend fun getManga(
        @Path("id") id: String,
        @Query("includes[]") includes: List<String>? = listOf("cover_art"),
    ): Response<Manga.MangaResponse>

    @GET("/manga/{id}/aggregate")
    suspend fun getMangaWithVolumesAndChapters(
        @Path("id") id: String,
        @Query("translatedLanguage[]") includes: List<String>? = listOf("en"),
    ): Response<Manga.MangasResponse>

}