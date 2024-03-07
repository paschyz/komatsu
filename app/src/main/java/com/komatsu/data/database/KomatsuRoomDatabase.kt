package com.komatsu.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.komatsu.data.database.converters.MangaIdListConverter
import com.komatsu.data.database.dao.LocalMangaDao
import com.komatsu.data.database.dao.MangaCollectionCrossRefDao
import com.komatsu.data.database.dao.MangaCollectionDao
import com.komatsu.data.database.dao.MangaDao
import com.komatsu.data.database.entities.LocalMangaEntity
import com.komatsu.data.database.entities.MangaCollectionEntity
import com.komatsu.data.database.entities.MangaWithCollections
import com.komatsu.data.database.relations.MangaCollectionCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        MangaCollectionEntity::class,
        LocalMangaEntity::class,
        MangaCollectionCrossRef::class,
    ], version = 1, exportSchema = false
)
@TypeConverters(MangaIdListConverter::class)
abstract class KomatsuRoomDatabase : RoomDatabase() {
    abstract fun mangaCollectionDao(): MangaCollectionDao
    abstract fun localMangaDao(): LocalMangaDao
    abstract fun mangaCollectionCrossRefDao(): MangaCollectionCrossRefDao

    private class KomatsuRoomDatabaseCallback(
        private val scope: CoroutineScope,
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch { populateDatabase(database) }
            }
        }

        suspend fun populateDatabase(database: KomatsuRoomDatabase) {
            val mangaCollectionDao = database.mangaCollectionDao()
            val mangaDao = database.localMangaDao()
            val mangaCollectionCrossRefDao = database.mangaCollectionCrossRefDao()

            val prePopulatedMangaCollections = arrayOf(
                "Saved",
                "Reading",
                "Completed",
                "On Hold",
                "Dropped",
                "Plan to Read",
            )

            for (mangaCollection in prePopulatedMangaCollections) {

                val mangaCollection =
                    MangaCollectionEntity(
                        name = mangaCollection,
                    )

                mangaCollectionDao.insert(mangaCollection)

                mangaDao.insertManga(
                    LocalMangaEntity(
                        mangaId = "c52b2ce3-7f95-469c-96b0-479524fb7a1a",
                    )
                )


                mangaCollectionCrossRefDao.insertCrossRef(
                    MangaCollectionCrossRef(
                        mangaId = "c52b2ce3-7f95-469c-96b0-479524fb7a1a",
                        collectionId = mangaCollection.collectionId
                    )
                )
            }
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
                            .fallbackToDestructiveMigration()
                            .addCallback(KomatsuRoomDatabaseCallback(scope))
                            .build()
                    INSTANCE = instance
                    instance.openHelper.writableDatabase
                    instance
                }
        }
    }
}
