package com.fiveguysburger.emodiary.core.service

import com.fiveguysburger.emodiary.core.entity.UserLoginDetails
import com.fiveguysburger.emodiary.core.entity.Users

interface UsersService {
    /**
     * 회원가입
     * @param email 사용자 이메일
     * @param providerId 소셜 로그인 제공자 ID
     * @param loginMethod 로그인 방법 (예: "google", "kakao", "naver")
     * @return 생성된 사용자 정보
     */
    fun registerUser(
        email: String,
        providerId: String,
        loginMethod: String,
    ): Users

    /**
     * 이메일로 사용자 검색
     * @param email 검색할 이메일
     * @return 사용자 정보 (없으면 null)
     */
    fun findUserByEmail(email: String): Users?

    /**
     * 로그인 시간 업데이트 (로그인 이력 추가)
     * @param userId 사용자 ID
     * @param loginMethod 로그인 방법
     * @param providerId 소셜 로그인 제공자 ID
     * @return 생성된 로그인 상세 정보
     */
    fun updateLoginTime(
        userId: Int,
        loginMethod: String,
        providerId: String,
    ): UserLoginDetails

    /**
     * 사용자 알람 상태 업데이트
     * @param userId 사용자 ID
     * @param alarmStatus 알람 상태
     * @return 업데이트된 사용자 정보
     */
    fun updateAlarmStatus(
        userId: Int,
        alarmStatus: String,
    ): Users

    /**
     * 사용자 ID로 사용자 검색
     * @param userId 사용자 ID
     * @return 사용자 정보 (없으면 null)
     */
    fun findUserById(userId: Int): Users?
}
