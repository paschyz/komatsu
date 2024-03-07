package com.komatsu.di.modules

import com.komatsu.data.database.KomatsuRoomDatabase
import org.koin.dsl.module

val DatabaseModule = module {
    single { KomatsuRoomDatabase.getDatabase(get(), get()) }
    single { get<KomatsuRoomDatabase>().mangaCollectionDao() }
    single { get<KomatsuRoomDatabase>().localMangaDao() }
    single { get<KomatsuRoomDatabase>().mangaCollectionCrossRefDao() }
}
