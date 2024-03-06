package com.example.komatsu.data.models

data class MangaCollection(val id: String, val name: String, val editable: Boolean = true, val deletable: Boolean = true, val mangas: List<MangaId> = emptyList()) {
    fun removeManga(mangaId: MangaId): MangaCollection {
        return copy(mangas = mangas.filter { it != mangaId })
    }

    fun addManga(mangaId: MangaId): MangaCollection {
        return copy(mangas = mangas + mangaId)
    }
}
