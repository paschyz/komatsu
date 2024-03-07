package com.example.komatsu.models


import com.google.gson.annotations.SerializedName

data class Manga(
    val id: String,
    val type: String,
    val attributes: MangaAttributes,
    val relationships: List<Relationship>,
) {

    companion object {
        val EMPTY = Manga(
            id = "",
            type = "",
            attributes = MangaAttributes(
                title = mapOf(),
                altTitles = emptyList(),
                description = mapOf(),
                language = "",
                demographic = null,
                year = null,
                status = null,
                contentRating = ContentRating.NONE,
                tags = emptyList(),
            ),
            relationships = emptyList()
        )

        fun getCoverArtFilename(manga: Manga): String? {
            return manga.relationships.find { it.type == "cover_art" }?.attributes?.get("fileName") as? String
        }

    }

    data class MangasResponse(
        val result: String,
        val response: String,
        val total: Int,
        val limit: Int,
        val offset: Int,
        val data: List<Manga>,
    )

    data class MangaResponse(
        val result: String,
        val response: String,
        val data: Manga,
    )

    enum class PublicationDemographic {
        @SerializedName("shounen")
        SHONEN,

        @SerializedName("shoujo")
        SHOUJO,

        @SerializedName("josei")
        JOSEI,

        @SerializedName("seinen")
        SEINEN,
        NONE
    }

    enum class Status {
        @SerializedName("ongoing")
        ONGOING,

        @SerializedName("completed")
        COMPLETED,

        @SerializedName("hiatus")
        CANCELLED,

        @SerializedName("hiatus")
        HIATUS,
        NONE
    }

    enum class ContentRating {
        @SerializedName("safe")
        SAFE,

        @SerializedName("suggestive")
        SUGGESTIVE,

        @SerializedName("erotica")
        EROTICA,

        @SerializedName("pornographic")
        PORNOGRAPHIC,
        NONE
    }

    data class Tag(
        val id: String,
        val type: String,
        val attributes: TagAttributes,
        val relationships: List<Relationship>,
    )

    data class TagAttributes(
        val name: Map<String, String>,
        val description: Map<String, String>,
        val group: String,
    )

    data class Relationship(
        val id: String,
        val type: String,
        val attributes: Map<String, Any?>,
    )

    data class MangaAttributes(
        val title: Map<String, String>,
        val altTitles: List<Map<String, String>>,
        val description: Map<String, String>,
        @SerializedName("originalLanguage")
        val language: String,
        @SerializedName("publicationDemographic")
        val demographic: PublicationDemographic?,
        val year: Int?,
        val status: Status?,
        val contentRating: ContentRating,
        val tags: List<Tag>,
    )
}
