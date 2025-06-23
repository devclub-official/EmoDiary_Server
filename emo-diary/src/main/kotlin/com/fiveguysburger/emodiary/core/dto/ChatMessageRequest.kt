package com.fiveguysburger.emodiary.core.dto

data class ChatMessageRequest(val message: String)

data class ChatMessageResponse(
    val chatRoomId: String,
    val message: String,
    val sender: String,
)
