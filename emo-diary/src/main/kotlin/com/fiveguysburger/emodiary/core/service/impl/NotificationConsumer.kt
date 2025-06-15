package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.dto.NotificationMessageDto
import com.fiveguysburger.emodiary.core.service.FcmService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class NotificationConsumer(
    private val fcmService: FcmService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @RabbitListener(queues = ["\${rabbitmq.notification.queue}"])
    fun handleNotification(notificationMessage: NotificationMessageDto) {
        try {
            logger.info("알림 메시지 수신: {}", notificationMessage)
            fcmService.processNotification(notificationMessage)
        } catch (e: Exception) {
            logger.error("알림 처리 중 오류 발생: {}", e.message, e)
            throw e
        }
    }
}
