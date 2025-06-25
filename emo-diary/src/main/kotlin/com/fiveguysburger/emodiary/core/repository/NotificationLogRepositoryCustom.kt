package com.fiveguysburger.emodiary.core.repository

import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import java.time.LocalDate

interface NotificationLogRepositoryCustom {
    /**
     * 특정 상태의 알림들을 조회합니다.
     * @param status 조회할 알림 상태
     * @return 해당 상태의 알림 로그 목록
     */
    fun findByNotificationStatus(status: NotificationStatus): List<NotificationLog>

    /**
     * 알림 발송 상태를 업데이트합니다.
     * @param id 알림 로그 ID
     * @param status 변경할 상태
     * @param fcmMessageId FCM 메시지 ID (선택)
     * @param errorMessage 에러 메시지 (선택)
     */
    fun updateNotificationStatus(
        id: String,
        status: NotificationStatus,
        fcmMessageId: String?,
        errorMessage: String?,
    )

    /**
     * 특정 사용자의 알림 이력을 생성일시 기준 내림차순으로 조회합니다.
     * @param userId 사용자 ID
     * @return 해당 사용자의 알림 로그 목록
     */
    fun findByUserIdOrderByCreatedAtDesc(userId: Int): List<NotificationLog>

    /**
     * 사용자 ID와 템플릿 ID로 알림 로그를 생성일시 기준 내림차순으로 조회합니다.
     * @param userId 사용자 ID
     * @param templateId 템플릿 ID
     * @return 해당 조건의 알림 로그 목록
     */
    fun findByUserIdAndTemplateIdOrderByCreatedAtDesc(
        userId: Int,
        templateId: Int,
    ): List<NotificationLog>

    /**
     * 특정 일수 이상 지난 알림 로그를 삭제합니다.
     * @param cutoffDate 삭제할 기준 날짜
     * @return 삭제된 알림 로그 수
     */
    fun deleteOldNotificationLogs(cutoffDate: LocalDate): Int
}
