package com.fiveguysburger.emodiary.core.service

import com.fiveguysburger.emodiary.core.dto.request.FcmTokenRequest

interface FcmTokenService {
    /**
     * FCM 토큰을 저장합니다.
     * @param request FCM 토큰 저장 요청
     */
    fun saveFcmToken(request: FcmTokenRequest)

    /**
     * 사용자의 FCM 토큰을 삭제합니다.
     * @param userId 사용자 ID
     */
    fun deleteFcmToken(userId: Int)

    /**
     * 사용자의 FCM 토큰을 조회합니다.
     * @param userId 사용자 ID
     * @return FCM 토큰
     */
    fun getFcmToken(userId: Int): String?
}
