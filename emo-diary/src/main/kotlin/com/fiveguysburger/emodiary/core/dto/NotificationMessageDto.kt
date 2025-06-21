package com.fiveguysburger.emodiary.core.dto

import com.fiveguysburger.emodiary.core.enums.NotificationType

data class NotificationMessageDto(
    val userId: Int,
    val templateId: Int,
    val notificationType: NotificationType,
    val token: String,
    val data: Map<String, String> = emptyMap(),
)
