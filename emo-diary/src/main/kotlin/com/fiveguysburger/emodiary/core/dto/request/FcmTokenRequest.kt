package com.fiveguysburger.emodiary.core.dto.request

data class FcmTokenRequest(
    val userId: Int,
    val fcmToken: String,
)
