package com.fiveguysburger.emodiary.core.dto

import com.fiveguysburger.emodiary.core.enums.NotificationType
import java.time.LocalDateTime

data class FcmMessageDto(
    val userId: Int,
    val token: String,
    val notificationType: NotificationType,
    val data: Map<String, String>,
    val sentAt: LocalDateTime = LocalDateTime.now(),
) {
    class Builder {
        private var userId: Int = 0
        private var token: String = ""
        private var notificationType: NotificationType? = null
        private var data: Map<String, String> = emptyMap()
        private var sentAt: LocalDateTime = LocalDateTime.now()

        fun userId(userId: Int) = apply { this.userId = userId }

        fun token(token: String) = apply { this.token = token }

        fun notificationType(notificationType: NotificationType) = apply { this.notificationType = notificationType }

        fun data(data: Map<String, String>) = apply { this.data = data }

        fun sentAt(sentAt: LocalDateTime) = apply { this.sentAt = sentAt }

        fun build(): FcmMessageDto {
            require(userId > 0) { "사용자 ID는 0보다 커야 합니다." }
            require(token.isNotBlank()) { "토큰은 비어있을 수 없습니다." }
            require(token.matches(Regex("^[A-Za-z0-9-_]+$"))) { "유효하지 않은 FCM 토큰 형식입니다." }
            require(notificationType != null) { "알림 유형은 필수입니다." }

            // 데이터 맵 검증
            data.forEach { (key, value) ->
                require(key.isNotBlank()) { "데이터 키는 비어있을 수 없습니다." }
                require(key.length <= 50) { "데이터 키는 50자를 초과할 수 없습니다." }
                require(value.length <= 100) { "데이터 값은 100자를 초과할 수 없습니다." }
            }

            return FcmMessageDto(
                userId = userId,
                token = token,
                notificationType = notificationType!!,
                data = data,
                sentAt = sentAt,
            )
        }
    }

    companion object {
        fun builder() = Builder()
    }
}
