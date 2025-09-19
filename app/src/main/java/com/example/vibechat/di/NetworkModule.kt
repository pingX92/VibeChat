package com.example.vibechat.di

import com.example.vibechat.data.remote.OpenAiApi
import com.example.vibechat.data.remote.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = ""
    private const val API_KEY = ""

    @Provides
    @Singleton
    fun provideOpenAiApi(): OpenAiApi {
        return RetrofitClient.create(BASE_URL, API_KEY)
    }
}