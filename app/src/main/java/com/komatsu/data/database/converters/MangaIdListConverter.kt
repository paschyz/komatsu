package com.komatsu.data.database.converters

import androidx.room.TypeConverter
import com.komatsu.data.models.MangaId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MangaIdListConverter {
    @TypeConverter
    fun fromMangaIdList(mangaIds: List<MangaId>): String {
        val gson = Gson()
        return gson.toJson(mangaIds)
    }

    @TypeConverter
    fun toMangaIdList(mangaIdString: String): List<MangaId> {
        val gson = Gson()
        val type = object : TypeToken<List<MangaId>>() {}.type
        return gson.fromJson(mangaIdString, type)
    }
}
