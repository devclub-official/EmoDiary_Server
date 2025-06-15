package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.enums.NotificationType
import com.fiveguysburger.emodiary.core.repository.NotificationLogRepository
import com.fiveguysburger.emodiary.core.service.NotificationLogService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class NotificationLogServiceImpl(
    private val notificationLogRepository: NotificationLogRepository,
) : NotificationLogService {
    /**
     * 새로운 알림 로그를 생성합니다.
     * @param userId 알림을 받을 사용자 ID
     * @param templateId 사용할 알림 템플릿 ID
     * @param notificationType 알림 유형
     * @return 생성된 알림 로그
     */
    @Transactional
    override fun createNotificationLog(
        userId: Int,
        templateId: Int,
        notificationType: NotificationType,
    ): NotificationLog {
        val now = LocalDateTime.now()
        val notificationLog =
            NotificationLog(
                userId = userId,
                sentAt = now,
                templateId = templateId,
                notificationType = notificationType,
                notificationStatus = NotificationStatus.PENDING,
            )
        return notificationLogRepository.save(notificationLog)
    }

    /**
     * 사용자 ID와 템플릿 ID로 알림 로그를 조회합니다.
     * @param userId 사용자 ID
     * @param templateId 템플릿 ID
     * @return 조회된 알림 로그, 없으면 null
     */
    @Transactional(readOnly = true)
    override fun findByUserIdAndTemplateId(
        userId: Int,
        templateId: Int,
    ): NotificationLog? =
        notificationLogRepository
            .findByUserIdAndTemplateIdOrderByCreatedAtDesc(userId, templateId)
            .firstOrNull()

    /**
     * 특정 상태의 알림들을 조회합니다.
     * @param status 조회할 알림 상태
     * @return 해당 상태의 알림 로그 목록
     */
    @Transactional(readOnly = true)
    override fun findByNotificationStatus(status: NotificationStatus): List<NotificationLog> =
        notificationLogRepository.findByNotificationStatus(status)

    /**
     * 일주일 이상 로그인하지 않은 사용자들을 조회합니다.
     * @return 장기 미접속 사용자 ID 목록
     */
    @Transactional(readOnly = true)
    override fun findInactiveUsers(): List<Int> {
        val oneWeekAgo = LocalDate.now().minusWeeks(1)
        return notificationLogRepository.findInactiveUsers(oneWeekAgo)
    }

    /**
     * 알림 발송 상태를 업데이트합니다.
     * @param id 알림 로그 ID
     * @param status 변경할 상태
     * @param fcmMessageId FCM 메시지 ID (선택)
     * @param errorMessage 에러 메시지 (선택)
     */
    @Transactional
    override fun updateNotificationStatus(
        id: Long,
        status: NotificationStatus,
        fcmMessageId: String?,
        errorMessage: String?,
    ) {
        notificationLogRepository.updateNotificationStatus(
            id = id,
            status = status,
            fcmMessageId = fcmMessageId,
            errorMessage = errorMessage,
        )
    }

    /**
     * 특정 사용자의 알림 이력을 조회합니다.
     * @param userId 사용자 ID
     * @return 해당 사용자의 알림 로그 목록
     */
    @Transactional(readOnly = true)
    override fun findUserNotificationHistory(userId: Int): List<NotificationLog> =
        notificationLogRepository.findByUserIdOrderByCreatedAtDesc(userId)

    /**
     * 특정 일수 이상 지난 알림 로그를 삭제합니다.
     * @param days 삭제할 기준 일수
     * @return 삭제된 알림 로그 수
     */
    @Transactional
    override fun deleteOldNotificationLogs(days: Int): Int {
        val cutoffDate = LocalDate.now().minusDays(days.toLong())
        return notificationLogRepository.deleteOldNotificationLogs(cutoffDate)
    }

    /**
     * 특정 알림 로그를 삭제합니다.
     * @param id 삭제할 알림 로그 ID
     */
    @Transactional
    override fun deleteNotificationLog(id: Long) {
        notificationLogRepository.deleteById(id)
    }
}
