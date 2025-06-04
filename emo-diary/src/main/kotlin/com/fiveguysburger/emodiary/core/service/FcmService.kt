package com.fiveguysburger.emodiary.core.service

import com.fiveguysburger.emodiary.core.dto.FcmMessageDto
import kotlinx.coroutines.flow.Flow

class FcmException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

interface FcmService {
    /**
     * FCM 메시지를 동기적으로 전송합니다.
     * @param message 전송할 FCM 메시지
     * @return 전송된 메시지 ID
     * @throws FcmException 메시지 전송 실패 시
     */
    fun sendMessage(message: FcmMessageDto): String

    /**
     * FCM 메시지를 비동기적으로 전송합니다.
     * @param message 전송할 FCM 메시지
     * @return 전송된 메시지 ID
     */
    suspend fun sendMessageAsync(message: FcmMessageDto): String

    /**
     * 여러 FCM 메시지를 비동기적으로 전송합니다.
     * @param messages 전송할 FCM 메시지 목록
     * @return 전송된 메시지 ID들의 Flow
     */
    fun sendMessagesAsync(messages: List<FcmMessageDto>): Flow<String>
} 