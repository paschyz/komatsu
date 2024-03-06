package com.example.komatsu.data.database

import androidx.room.Database
import androidx.room.RoomDatabase.Callback
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.komatsu.data.database.converters.MangaIdListConverter
import com.example.komatsu.data.database.dao.MangaCollectionDao
import com.example.komatsu.data.database.dao.MangaDao
import com.example.komatsu.data.database.entities.MangaCollectionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.util.Log

@Database(entities = [MangaCollectionEntity::class], version = 1)
@TypeConverters(MangaIdListConverter::class)
abstract class KomatsuRoomDatabase : androidx.room.RoomDatabase() {
    abstract fun mangaDao(): MangaDao
    abstract fun mangaCollectionDao(): MangaCollectionDao

    private class KomatsuRoomDatabaseCallback(
        private val scope: CoroutineScope,
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch { populateDatabase(database.mangaCollectionDao()) }
            }
        }

        fun populateDatabase(mangaCollectionDao: MangaCollectionDao) {
            val mangaCollection = MangaCollectionEntity("1", "Manga Collection 1")
            mangaCollectionDao.insert(mangaCollection)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: KomatsuRoomDatabase? = null

        fun getDatabase(
            context: android.content.Context,
            scope: CoroutineScope,
        ): KomatsuRoomDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    val instance =
                        androidx.room.Room.databaseBuilder(
                            context.applicationContext,
                            KomatsuRoomDatabase::class.java,
                            "komatsu_database"
                        )
                            .addCallback(KomatsuRoomDatabaseCallback(scope))
                            .build()
                    INSTANCE = instance
                    instance.openHelper.writableDatabase
                    instance
                }
        }
    }
}

