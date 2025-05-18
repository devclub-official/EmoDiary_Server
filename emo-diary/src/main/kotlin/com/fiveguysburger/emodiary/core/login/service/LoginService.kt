package com.fiveguysburger.emodiary.core.login.service

interface LoginService {
    fun loginWithGoogle(code: String)

    fun loginWithKakao(code: String)
}
