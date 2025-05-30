package com.fiveguysburger.emodiary.core.repository

import com.fiveguysburger.emodiary.core.entity.NotificationTemplate

interface NotificationTemplateRepositoryCustom {
    fun findByNotificationType(notificationType: Int): NotificationTemplate?

    fun findAllActive(): List<NotificationTemplate>
}
