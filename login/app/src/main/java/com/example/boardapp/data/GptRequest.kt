package com.example.boardapp.data
data class ChatMessage(
    val content: String,
    val isUser: Boolean
)
data class GptRequest(
    val prompt: String,
    val temperature: Double,
    val maxTokens: Int
)

data class GptResponse(
    val choices: List<GptChoice>
)

data class GptChoice(
    val text: String
)