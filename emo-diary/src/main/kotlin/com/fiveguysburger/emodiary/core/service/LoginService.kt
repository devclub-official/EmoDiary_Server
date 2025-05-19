package com.fiveguysburger.emodiary.core.service

interface LoginService {
    fun loginWithGoogle(code: String)

    fun loginWithKakao1(code: String)
}
