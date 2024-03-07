package com.komatsu.di.modules

import android.content.Context
import com.komatsu.R
import com.komatsu.data.api.ApiService
import com.komatsu.di.baseUrl
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.android.ext.koin.androidContext

val NetWorkModule = module {
    single { provideRetrofit(androidContext()) }
    single { provideMangaApi(get()) }
}

fun provideRetrofit(context: Context): Retrofit {
    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okhttp3.OkHttpClient.Builder().addInterceptor(interceptor).build())
        .build()
}

fun provideMangaApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
