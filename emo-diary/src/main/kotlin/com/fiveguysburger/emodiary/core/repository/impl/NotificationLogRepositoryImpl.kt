package com.fiveguysburger.emodiary.core.repository.impl

import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.entity.QNotificationLog
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.repository.NotificationLogRepositoryCustom
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Repository
class NotificationLogRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : NotificationLogRepositoryCustom {
    /**
     * 특정 상태의 알림들을 조회합니다.
     * @param status 조회할 알림 상태
     * @return 해당 상태의 알림 로그 목록
     */
    override fun findByNotificationStatus(status: NotificationStatus): List<NotificationLog> =
        queryFactory
            .selectFrom(QNotificationLog.notificationLog)
            .where(QNotificationLog.notificationLog.notificationStatus.eq(status))
            .fetch()

    /**
     * 특정 날짜 이후에 알림이 없는 사용자들을 조회합니다.
     * @param cutoffDate 기준 날짜
     * @return 장기 미접속 사용자 ID 목록
     */
    override fun findInactiveUsers(cutoffDate: LocalDate): List<Int> =
        queryFactory
            .select(QNotificationLog.notificationLog.userId)
            .from(QNotificationLog.notificationLog)
            .where(
                QNotificationLog.notificationLog.sentAt.before(cutoffDate.atStartOfDay()),
                QNotificationLog.notificationLog.userId.notIn(
                    queryFactory
                        .select(QNotificationLog.notificationLog.userId)
                        .from(QNotificationLog.notificationLog)
                        .where(QNotificationLog.notificationLog.sentAt.after(cutoffDate.atStartOfDay())),
                ),
            ).distinct()
            .fetch()

    /**
     * 알림 발송 상태를 업데이트합니다.
     * @param id 알림 로그 ID
     * @param status 변경할 상태
     * @param fcmMessageId FCM 메시지 ID (선택)
     * @param errorMessage 에러 메시지 (선택)
     */
    @Transactional
    override fun updateNotificationStatus(
        id: String,
        status: NotificationStatus,
        fcmMessageId: String?,
        errorMessage: String?,
    ) {
        queryFactory
            .update(QNotificationLog.notificationLog)
            .set(QNotificationLog.notificationLog.notificationStatus, status)
            .set(QNotificationLog.notificationLog.fcmMessageId, fcmMessageId)
            .set(QNotificationLog.notificationLog.errorMessage, errorMessage)
            .where(QNotificationLog.notificationLog.id.eq(id))
            .execute()
    }

    /**
     * 특정 사용자의 알림 이력을 생성일시 기준 내림차순으로 조회합니다.
     * @param userId 사용자 ID
     * @return 해당 사용자의 알림 로그 목록
     */
    override fun findByUserIdOrderByCreatedAtDesc(userId: Int): List<NotificationLog> =
        queryFactory
            .selectFrom(QNotificationLog.notificationLog)
            .where(QNotificationLog.notificationLog.userId.eq(userId))
            .orderBy(QNotificationLog.notificationLog.createdAt.desc())
            .fetch()

    /**
     * 사용자 ID와 템플릿 ID로 알림 로그를 생성일시 기준 내림차순으로 조회합니다.
     * @param userId 사용자 ID
     * @param templateId 템플릿 ID
     * @return 해당 조건의 알림 로그 목록
     */
    override fun findByUserIdAndTemplateIdOrderByCreatedAtDesc(
        userId: Int,
        templateId: Int,
    ): List<NotificationLog> =
        queryFactory
            .selectFrom(QNotificationLog.notificationLog)
            .where(
                QNotificationLog.notificationLog.userId.eq(userId),
                QNotificationLog.notificationLog.templateId.eq(templateId),
            ).orderBy(QNotificationLog.notificationLog.createdAt.desc())
            .fetch()

    /**
     * 특정 날짜 이전의 알림 로그를 삭제합니다.
     * @param cutoffDate 삭제할 기준 날짜
     * @return 삭제된 알림 로그 수
     */
    @Transactional
    override fun deleteOldNotificationLogs(cutoffDate: LocalDate): Int =
        queryFactory
            .delete(QNotificationLog.notificationLog)
            .where(QNotificationLog.notificationLog.sentAt.before(cutoffDate.atStartOfDay()))
            .execute()
            .toInt()
}
