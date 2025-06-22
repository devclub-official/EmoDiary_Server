package com.fiveguysburger.emodiary.core.dto

data class ChatMessageRequest(val message: String)

data class ChatMessageResponse(
    val dailyChatId: String,
    val message: String,
    val sender: String,
)
