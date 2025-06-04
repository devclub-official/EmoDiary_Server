package com.fiveguysburger.emodiary.core.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "알림 유형")
enum class NotificationType(
    @Schema(description = "알림 유형 코드", example = "1")
    val value: Int
) {
    @Schema(description = "일기 작성 유도 알림")
    DIARY_REMINDER(1), // 일기 작성 유도 알림
    
    @Schema(description = "장기 미접속 사용자 알림")
    INACTIVE_DIARY(2), // 장기 미접속 사용자 알림
    ;

    companion object {
        fun fromValue(value: Int): NotificationType {
            return values().find { it.value == value }
                ?: throw IllegalArgumentException("Invalid notification type value: $value")
        }
    }
}
