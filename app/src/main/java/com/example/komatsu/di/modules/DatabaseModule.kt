package com.example.komatsu.di.modules

import com.example.komatsu.data.database.KomatsuRoomDatabase
import org.koin.dsl.module

val DatabaseModule = module {
    single { KomatsuRoomDatabase.getDatabase(get(), get()) }
    single { get<KomatsuRoomDatabase>().mangaCollectionDao() }
    single { get<KomatsuRoomDatabase>().mangaDao() }
}
