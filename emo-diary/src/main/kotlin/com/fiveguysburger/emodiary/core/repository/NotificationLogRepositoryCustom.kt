package com.fiveguysburger.emodiary.core.repository

import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import java.time.LocalDateTime

interface NotificationLogRepositoryCustom {
    /**
     * 특정 상태의 알림들을 조회합니다.
     * @param status 조회할 알림 상태
     * @return 해당 상태의 알림 로그 목록
     */
    fun findByNotificationStatus(status: NotificationStatus): List<NotificationLog>

    /**
     * 마지막 로그인 시각이 지정된 시각보다 이전인 사용자들을 조회합니다.
     * @param lastLoginBefore 기준 시각
     * @return 장기 미접속 사용자 ID 목록
     */
    fun findInactiveUsers(lastLoginBefore: LocalDateTime): List<Int>

    /**
     * 알림 발송 상태를 업데이트합니다.
     * @param userId 사용자 ID
     * @param sentAt 발송 시각
     * @param templateId 템플릿 ID
     * @param status 변경할 상태
     * @param fcmMessageId FCM 메시지 ID (선택)
     * @param errorMessage 에러 메시지 (선택)
     */
    fun updateNotificationStatus(
        userId: Int,
        sentAt: LocalDateTime,
        templateId: Int,
        status: NotificationStatus,
        fcmMessageId: String? = null,
        errorMessage: String? = null,
    )

    /**
     * 특정 사용자의 알림 이력을 생성일시 기준 내림차순으로 조회합니다.
     * @param userId 사용자 ID
     * @return 해당 사용자의 알림 로그 목록
     */
    fun findByUserIdOrderByCreatedAtDesc(userId: Int): List<NotificationLog>
}
