package com.example.vibechat.di

import com.example.vibechat.data.remote.OpenAiApi
import com.example.vibechat.data.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    private const val DEPLOYMENT = ""
    private const val API_VERSION = ""

    @Provides
    @Singleton
    fun provideChatRepository(api: OpenAiApi): ChatRepository {
        return ChatRepository(api, DEPLOYMENT, API_VERSION)
    }
}