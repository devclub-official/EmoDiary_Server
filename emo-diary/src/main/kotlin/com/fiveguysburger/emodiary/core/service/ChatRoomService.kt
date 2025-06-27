package com.fiveguysburger.emodiary.core.service

import com.fiveguysburger.emodiary.core.dto.AnalysisResponse
import com.fiveguysburger.emodiary.core.dto.ApiResponse
import com.fiveguysburger.emodiary.core.dto.ChatMessageResponse
import com.fiveguysburger.emodiary.core.dto.CreateDiaryRoomResponse
import com.fiveguysburger.emodiary.core.dto.Messages

interface ChatRoomService {
    fun createChatRoom(userId: String): ApiResponse<CreateDiaryRoomResponse>

    fun sendMessage(
        chatroomId: String,
        userId: String,
        message: String,
    ): ApiResponse<ChatMessageResponse>

    fun getAllMessages(
        chatroomId: String,
        userId: String,
    ): ApiResponse<Messages>

    fun requestDiaryAnalysis(
        chatroomId: String,
        userId: String,
    ): ApiResponse<AnalysisResponse>
}
