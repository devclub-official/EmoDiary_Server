package com.fiveguysburger.emodiary.core.dto

import java.time.LocalDateTime

data class LoginHistoryResponseDto(
    val loginAt: LocalDateTime? = null,
    val loginMethod: String? = null,
)
