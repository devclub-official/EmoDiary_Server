package com.fiveguysburger.emodiary.core.repository.impl

import com.fiveguysburger.emodiary.core.entity.NotificationTemplate
import com.fiveguysburger.emodiary.core.entity.QNotificationTemplate
import com.fiveguysburger.emodiary.core.repository.NotificationTemplateRepositoryCustom
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class NotificationTemplateRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : NotificationTemplateRepositoryCustom {
    override fun findByNotificationType(notificationType: Int): NotificationTemplate? =
        queryFactory
            .selectFrom(QNotificationTemplate.notificationTemplate)
            .where(
                QNotificationTemplate.notificationTemplate.notificationType.eq(notificationType),
                QNotificationTemplate.notificationTemplate.isActive.eq(true),
            ).fetchOne()

    override fun findAllActive(): List<NotificationTemplate> =
        queryFactory
            .selectFrom(QNotificationTemplate.notificationTemplate)
            .where(QNotificationTemplate.notificationTemplate.isActive.eq(true))
            .fetch()
} 
