package com.fiveguysburger.emodiary.core.service

import com.fiveguysburger.emodiary.core.dto.TokenResponse

interface LoginService {
    fun loginWithGoogle(code: String): TokenResponse

    fun loginWithKakao(code: String): TokenResponse

    fun logout(token: String): Boolean

    fun generateGoogleAuthUrl(): String

    fun generateKakaoAuthUrl(): String
}
