package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.dto.FcmMessageDto
import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.service.FcmException
import com.fiveguysburger.emodiary.core.service.FcmService
import com.fiveguysburger.emodiary.core.service.NotificationLogService
import com.fiveguysburger.emodiary.core.service.NotificationTemplateService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FcmServiceImpl(
    private val firebaseMessaging: FirebaseMessaging,
    private val notificationTemplateService: NotificationTemplateService,
    private val notificationLogService: NotificationLogService,
) : FcmService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendMessage(message: FcmMessageDto): String {
        var notificationLog: NotificationLog? = null
        try {
            logger.info("FCM 메시지 전송 시작: {}", message)

            // 1. 템플릿 조회
            val template =
                notificationTemplateService.getTemplateByType(message.notificationType.value)
                    ?: throw FcmException("템플릿을 찾을 수 없습니다: ${message.notificationType}")

            // 2. 알림 로그 생성 (PENDING 상태로 시작)
            notificationLog =
                notificationLogService.createNotificationLog(
                    userId = message.userId,
                    templateId = template.id.toInt(),
                    notificationType = message.notificationType,
                )

            // 3. FCM 메시지 생성 및 전송
            val fcmMessage =
                Message
                    .builder()
                    .setNotification(
                        Notification
                            .builder()
                            .setTitle(template.title)
                            .setBody(template.content)
                            .build(),
                    ).setToken(message.token)
                    .putAllData(message.data)
                    .build()

            // 4. 전송 중 상태로 업데이트
            notificationLogService.updateNotificationStatus(
                id = notificationLog.id,
                status = NotificationStatus.SENDING,
            )

            val messageId = firebaseMessaging.send(fcmMessage)
            logger.info("FCM 메시지 전송 성공: messageId={}", messageId)

            // 5. 전송 완료 상태로 업데이트
            notificationLogService.updateNotificationStatus(
                id = notificationLog.id,
                status = NotificationStatus.SENT,
                fcmMessageId = messageId,
            )

            return messageId
        } catch (e: Exception) {
            logger.error("FCM 메시지 전송 실패: {}", e.message, e)

            // 6. 실패 상태로 업데이트
            notificationLog?.let {
                notificationLogService.updateNotificationStatus(
                    id = it.id,
                    status = NotificationStatus.FAILED,
                    errorMessage = e.message,
                )
            }

            throw FcmException("FCM 메시지 전송 실패: ${e.message}", e)
        }
    }

    override suspend fun sendMessageAsync(message: FcmMessageDto): String =
        withContext(Dispatchers.IO) {
            sendMessage(message)
        }

    override fun sendMessagesAsync(messages: List<FcmMessageDto>): Flow<String> =
        flow {
            val results = mutableListOf<Pair<FcmMessageDto, String>>()
            val failures = mutableListOf<Pair<FcmMessageDto, Exception>>()

            messages.forEach { message ->
                try {
                    val messageId = sendMessageAsync(message)
                    results.add(message to messageId)
                    emit(messageId)
                } catch (e: Exception) {
                    failures.add(message to e)
                    logger.error("FCM 메시지 전송 실패: message={}, error={}", message, e.message)
                }
            }

            if (failures.isNotEmpty()) {
                logger.warn(
                    "일부 FCM 메시지 전송 실패: total={}, success={}, failure={}",
                    messages.size,
                    results.size,
                    failures.size,
                )
            }
        }.flowOn(Dispatchers.IO)
}
