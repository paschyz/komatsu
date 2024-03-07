package com.komatsu.ui

import android.app.Application
import com.komatsu.di.modules.AppModule
import com.komatsu.di.modules.AuthModule
import com.komatsu.di.modules.DatabaseModule
import com.komatsu.di.modules.NetWorkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KomatsuApp : Application() {

    override fun onCreate() {
        super.onCreate()

//        deleteDatabase("komatsu_database")

        startKoin {
            androidLogger()
            androidContext(this@KomatsuApp)
            modules(listOf(AppModule, NetWorkModule, DatabaseModule, AuthModule))
        }
    }
}
