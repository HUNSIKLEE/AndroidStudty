package com.example.boardapp.ui.main

import com.example.boardapp.data.GptRequest
import com.example.boardapp.data.GptResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface GptApiService {
    @Headers("Content-Type: application/json")
    @POST("completions")
    suspend fun completePrompt(
        @Header("Authorization") apiKey: String,
        @Body request: GptRequest
    ): GptResponse
}