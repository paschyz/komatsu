package com.komatsu.di.modules

import com.komatsu.data.LoginDataSource
import com.komatsu.data.LoginRepository
import org.koin.dsl.module


val AuthModule = module {
    single { LoginDataSource() }
    single { LoginRepository(get()) }
}