package com.fiveguysburger.emodiary.core.dto

import com.fiveguysburger.emodiary.core.enums.NotificationType
import java.util.UUID

data class NotificationMessageDto(
    val messageId: String = UUID.randomUUID().toString(),
    val userId: Int,
    val templateId: Int,
    val notificationType: NotificationType,
    val token: String,
    val data: Map<String, String> = emptyMap(),
)
