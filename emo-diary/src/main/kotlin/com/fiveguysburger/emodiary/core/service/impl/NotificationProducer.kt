package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.dto.NotificationMessageDto
import com.fiveguysburger.emodiary.core.service.NotificationLogService
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.MessageDeliveryMode
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class NotificationProducer(
    private val rabbitTemplate: RabbitTemplate,
    private val notificationLogService: NotificationLogService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${rabbitmq.notification.exchange}")
    private lateinit var exchange: String

    @Value("\${rabbitmq.notification.routing-key}")
    private lateinit var routingKey: String

    fun sendNotification(notificationMessage: NotificationMessageDto) {
        try {
            // 1. DB에 로그 먼저 생성 (메시지 유실 방지)
            val notificationLog =
                notificationLogService.createNotificationLog(
                    userId = notificationMessage.userId,
                    templateId = notificationMessage.templateId,
                    notificationType = notificationMessage.notificationType,
                )

            // 2. 로그 ID를 메시지 ID로 사용(추적용)
            val messageWithLogId = notificationMessage.copy(messageId = notificationLog.id)

            logger.info("알림 메시지 전송 시작: userId=${notificationMessage.userId}, messageId=${messageWithLogId.messageId}")

            // 3. 메시지 전송
            rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                messageWithLogId,
                { message ->
                    message.messageProperties.apply {
                        // 메시지 지속성 보장
                        deliveryMode = MessageDeliveryMode.PERSISTENT
                        // 중복 처리 및 추적을 위한 메시지 ID
                        messageId = messageWithLogId.messageId
                    }
                    message
                },
            )

            logger.info("알림 메시지 전송 완료: messageId=${messageWithLogId.messageId}")
        } catch (e: Exception) {
            logger.error("알림 메시지 전송 실패: userId=${notificationMessage.userId}, 오류: ${e.message}", e)
            throw e
        }
    }
}
