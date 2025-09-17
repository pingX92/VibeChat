package com.example.vibechat.data.remote

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class ChatRequest(
    val messages: List<MessageParam>,
    val max_tokens: Int = 200
)

data class MessageParam(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: MessageParam
)

data class ContentWithEmotionResponse(
    val message: String,
    val emotion: String
)

interface OpenAiApi {
    @POST("openai/deployments/{deployment}/chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun getChatCompletion(
        @Path("deployment") deployment: String,
        @Query("api-version") apiVersion: String,
        @Body request: ChatRequest
    ): ChatResponse
}