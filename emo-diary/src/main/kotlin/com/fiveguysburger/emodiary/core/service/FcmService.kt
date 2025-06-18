package com.fiveguysburger.emodiary.core.service

import com.fiveguysburger.emodiary.core.dto.FcmMessageDto
import com.fiveguysburger.emodiary.core.dto.NotificationMessageDto

interface FcmService {
    /**
     * FCM 메시지를 동기적으로 전송합니다.
     * @param message 전송할 FCM 메시지
     * @return 전송된 메시지 ID
     */
    fun sendMessage(message: FcmMessageDto): String

    /**
     * RabbitMQ를 통해 전달된 알림 메시지를 처리합니다.
     * @param notificationMessage 처리할 알림 메시지
     * @return 전송된 메시지 ID
     */
    fun processNotification(notificationMessage: NotificationMessageDto): String
}
