package com.fiveguysburger.emodiary.core.service

import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.enums.NotificationType
import java.time.LocalDateTime

interface NotificationLogService {
    /**
     * 새로운 알림 로그를 생성합니다.
     * @param userId 알림을 받을 사용자 ID
     * @param templateId 사용할 알림 템플릿 ID
     * @param notificationType 알림 유형
     * @return 생성된 알림 로그
     */
    fun createNotificationLog(
        userId: Int,
        templateId: Int,
        notificationType: NotificationType
    ): NotificationLog

    /**
     * 특정 상태의 알림들을 조회합니다.
     * @param status 조회할 알림 상태
     * @return 해당 상태의 알림 로그 목록
     */
    fun findByNotificationStatus(status: NotificationStatus): List<NotificationLog>

    /**
     * 일주일 이상 로그인하지 않은 사용자들을 조회합니다.
     * @return 장기 미접속 사용자 ID 목록
     */
    fun findInactiveUsers(): List<Int>

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
        errorMessage: String? = null
    )

    /**
     * 특정 사용자의 알림 이력을 조회합니다.
     * @param userId 사용자 ID
     * @return 해당 사용자의 알림 로그 목록
     */
    fun findUserNotificationHistory(userId: Int): List<NotificationLog>
} 