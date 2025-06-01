package com.fiveguysburger.emodiary.core.enums

enum class NotificationType(val value: Int) {
    DIARY_REMINDER(1), // 일기 작성 유도 알림
    INACTIVE_DIARY(2), // 장기 미접속 사용자 알림
    ;

    companion object {
        fun fromValue(value: Int): NotificationType {
            return values().find { it.value == value }
                ?: throw IllegalArgumentException("Invalid notification type value: $value")
        }
    }
}
