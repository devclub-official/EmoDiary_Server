package com.fiveguysburger.emodiary.core.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "알림 템플릿 응답")
data class NotificationTemplateResponseDto(
    @Schema(description = "템플릿 ID")
    val id: Long,
    @Schema(description = "알림 유형 코드")
    val notificationType: Int,
    @Schema(description = "알림 제목")
    val title: String,
    @Schema(description = "알림 내용")
    val content: String,
    @Schema(description = "생성 일시")
    val createdAt: LocalDateTime,
    @Schema(description = "수정 일시")
    val updatedAt: LocalDateTime,
    @Schema(description = "활성화 여부")
    val isActive: Boolean,
) 