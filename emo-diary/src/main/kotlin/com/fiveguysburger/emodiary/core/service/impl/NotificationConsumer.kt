package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.dto.NotificationMessageDto
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.service.DuplicateMessageService
import com.fiveguysburger.emodiary.core.service.FcmService
import com.fiveguysburger.emodiary.core.service.NotificationLogService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class NotificationConsumer(
    private val fcmService: FcmService,
    private val notificationLogService: NotificationLogService,
    private val duplicateMessageService: DuplicateMessageService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @RabbitListener(queues = ["\${rabbitmq.notification.queue}"])
    fun handleNotification(notificationMessage: NotificationMessageDto) {
        logger.info("알림 메시지 수신: messageId=${notificationMessage.messageId}, userId=${notificationMessage.userId}")

        try {
            // 1. 중복 체크 (Redis 기반)
            if (duplicateMessageService.isDuplicate(notificationMessage.messageId)) {
                logger.warn("중복 메시지 감지: messageId=${notificationMessage.messageId}")
                return
            }

            // 2. 상태 업데이트 (SENDING)
            notificationLogService.updateNotificationStatus(
                id = notificationMessage.messageId,
                status = NotificationStatus.SENDING,
            )

            logger.info("알림 발송 시작: messageId=${notificationMessage.messageId}")

            // 3. FCM 발송
            val fcmResult = fcmService.processNotification(notificationMessage)

            // 4. 성공 시 상태 업데이트 (SENT)
            notificationLogService.updateNotificationStatus(
                id = notificationMessage.messageId,
                status = NotificationStatus.SENT,
                fcmMessageId = fcmResult,
            )

            logger.info("알림 발송 성공: messageId=${notificationMessage.messageId}, fcmMessageId=$fcmResult")
        } catch (e: Exception) {
            // 5. 실패 시 상태 업데이트 (FAILED)
            notificationLogService.updateNotificationStatus(
                id = notificationMessage.messageId,
                status = NotificationStatus.FAILED,
                errorMessage = e.message,
            )

            logger.error("알림 발송 실패: messageId=${notificationMessage.messageId}, 오류: ${e.message}", e)

            // 재시도를 위해 예외 재발생
            throw e
        }
    }
}
