package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.dto.FcmMessageDto
import com.fiveguysburger.emodiary.core.dto.NotificationMessageDto
import com.fiveguysburger.emodiary.core.exception.FcmException
import com.fiveguysburger.emodiary.core.service.FcmService
import com.fiveguysburger.emodiary.core.service.NotificationTemplateService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FcmServiceImpl(
    private val firebaseMessaging: FirebaseMessaging,
    private val notificationTemplateService: NotificationTemplateService,
    private val notificationProducer: NotificationProducer,
) : FcmService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendMessage(message: FcmMessageDto): String {
        try {
            logger.info("FCM 메시지 전송 시작: {}", message)

            // 1. 템플릿 조회
            val template =
                notificationTemplateService.getTemplateByType(message.notificationType.value)
                    ?: throw FcmException("템플릿을 찾을 수 없습니다: ${message.notificationType}")

            // 2. RabbitMQ를 통해 메시지 전송
            val notificationMessage =
                NotificationMessageDto(
                    userId = message.userId,
                    templateId = template.id.toInt(),
                    notificationType = message.notificationType,
                    token = message.token,
                    data = message.data,
                )
            notificationProducer.sendNotification(notificationMessage)

            return "message queued"
        } catch (e: Exception) {
            logger.error("FCM 메시지 전송 실패: ${e.message}", e)
            throw e
        }
    }

    override fun processNotification(notificationMessage: NotificationMessageDto): String {
        try {
            // 1. 템플릿 조회
            val template =
                notificationTemplateService.getTemplateByType(notificationMessage.notificationType.value)
                    ?: throw FcmException("템플릿을 찾을 수 없습니다: ${notificationMessage.notificationType}")

            // 2. FCM 메시지 생성 및 전송
            val fcmMessage =
                Message
                    .builder()
                    .setNotification(
                        Notification
                            .builder()
                            .setTitle(template.title)
                            .setBody(template.content)
                            .build(),
                    ).setToken(notificationMessage.token)
                    .putAllData(notificationMessage.data)
                    .build()

            val messageId = firebaseMessaging.send(fcmMessage)
            logger.info("FCM 메시지 전송 성공: notificationMessageId=${notificationMessage.messageId}")

            return messageId
        } catch (e: Exception) {
            logger.error("FCM 메시지 전송 실패: notificationMessageId=${notificationMessage.messageId}, 오류: ${e.message}")
            throw e
        }
    }
}
