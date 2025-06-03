package com.fiveguysburger.emodiary.core.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "알림 템플릿 생성 요청")
data class NotificationTemplateRequestDto(
    @Schema(
        description = "알림 유형 코드 (1: 일기 작성 유도 알림, 2: 장기 미접속 사용자 알림)",
        example = "1",
        allowableValues = ["1", "2"]
    )
    val notificationType: Int,
    @Schema(description = "알림 제목", example = "일기 작성 시간이에요!")
    val title: String,
    @Schema(description = "알림 내용", example = "오늘 하루는 어떠셨나요? 감정을 기록해보세요.")
    val content: String,
) 
