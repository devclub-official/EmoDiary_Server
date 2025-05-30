package com.fiveguysburger.emodiary.core.enums

enum class NotificationStatus(val value: Int) {
    PENDING(1), // 발송 대기
    SENDING(2), // 발송 중
    SENT(3), // 발송 완료
    FAILED(4), // 발송 실패
    ;

    companion object {
        fun fromValue(value: Int): NotificationStatus {
            return values().find { it.value == value }
                ?: throw IllegalArgumentException("Invalid notification status value: $value")
        }
    }
}
