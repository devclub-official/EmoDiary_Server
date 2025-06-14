package com.fiveguysburger.emodiary.core.service

import com.fiveguysburger.emodiary.core.dto.LoginHistoryResponseDto

interface MypageService {
    /**
     * 사용자 ID로 사용자 검색
     * @param userId 사용자 ID
     * @return 사용자 정보 (없으면 null)
     */
    fun findUserLoginHistoryById(userId: Int): LoginHistoryResponseDto?

    fun deleteUserById(userId: Int): Boolean

    fun updateAlarmStatus(
        userId: Int,
        alarmStatus: String,
    ): Boolean
}
