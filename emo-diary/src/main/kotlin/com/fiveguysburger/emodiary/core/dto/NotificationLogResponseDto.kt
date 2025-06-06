package com.fiveguysburger.emodiary.core.dto

import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.enums.NotificationType

data class NotificationLogResponseDto(
    val id: Long,
    val userId: Int,
    val sentAt: String,
    val templateId: Int,
    val notificationType: NotificationType,
    val notificationStatus: NotificationStatus,
    val fcmMessageId: String?,
    val errorMessage: String?,
) {
    companion object {
        fun from(notificationLog: NotificationLog): NotificationLogResponseDto =
            NotificationLogResponseDto(
                id = notificationLog.id,
                userId = notificationLog.userId,
                sentAt = notificationLog.sentAt.toString(),
                templateId = notificationLog.templateId,
                notificationType = notificationLog.notificationType,
                notificationStatus = notificationLog.notificationStatus,
                fcmMessageId = notificationLog.fcmMessageId,
                errorMessage = notificationLog.errorMessage,
            )
    }
}
