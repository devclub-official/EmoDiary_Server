package com.fiveguysburger.emodiary.core.repository.impl

import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.entity.QNotificationLog
import com.fiveguysburger.emodiary.core.entity.QUserLoginDetails
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.repository.NotificationLogRepositoryCustom
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

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
     * 마지막 로그인 시각이 지정된 시각보다 이전인 사용자들을 조회합니다.
     * @param lastLoginBefore 기준 시각
     * @return 장기 미접속 사용자 ID 목록
     */
    override fun findInactiveUsers(lastLoginBefore: LocalDateTime): List<Int> {
        val oneWeekAgo = LocalDateTime.now().minusWeeks(1)
        return queryFactory
            .select(QUserLoginDetails.userLoginDetails.userId)
            .from(QUserLoginDetails.userLoginDetails)
            .where(
                QUserLoginDetails.userLoginDetails.loginAt.lt(oneWeekAgo)
            )
            .groupBy(QUserLoginDetails.userLoginDetails.userId)
            .fetch()
    }

    /**
     * 알림 발송 상태를 업데이트합니다.
     * @param userId 사용자 ID
     * @param sentAt 발송 시각
     * @param templateId 템플릿 ID
     * @param status 변경할 상태
     * @param fcmMessageId FCM 메시지 ID (선택)
     * @param errorMessage 에러 메시지 (선택)
     */
    override fun updateNotificationStatus(
        userId: Int,
        sentAt: LocalDateTime,
        templateId: Int,
        status: NotificationStatus,
        fcmMessageId: String?,
        errorMessage: String?,
    ) {
        queryFactory
            .update(QNotificationLog.notificationLog)
            .set(QNotificationLog.notificationLog.notificationStatus, status)
            .set(QNotificationLog.notificationLog.fcmMessageId, fcmMessageId)
            .set(QNotificationLog.notificationLog.errorMessage, errorMessage)
            .where(
                QNotificationLog.notificationLog.userId.eq(userId),
                QNotificationLog.notificationLog.sentAt.eq(sentAt),
                QNotificationLog.notificationLog.templateId.eq(templateId),
            ).execute()
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
} 
