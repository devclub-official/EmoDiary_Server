package com.fiveguysburger.emodiary.core.constant

object NotificationConstants {
    const val NOTIFICATION_JOB = "notificationJob"
    const val MAX_NOTIFICATION_COUNT = 500 // FCM은 최대 500명의 사용자에게 동시에 알림을 보낼 수 있음
}

enum class NotificationType {
    DIARY_REMINDER,
    INACTIVE_DIARY,
}
