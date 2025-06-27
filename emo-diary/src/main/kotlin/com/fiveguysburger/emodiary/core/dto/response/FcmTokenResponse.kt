package com.fiveguysburger.emodiary.core.dto.response

data class FcmTokenResponse(
    val userId: Int,
    val fcmToken: String,
    val expiresAt: String,
)
