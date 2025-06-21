package com.fiveguysburger.emodiary.core.service

interface ChatRoomService {
    fun createChatRoom(userId: String): String

    fun sendMessage(
        chatRoomId: String,
        userId: String,
        message: String,
    ): String
}
