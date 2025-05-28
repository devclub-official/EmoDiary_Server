package com.fiveguysburger.emodiary.core.service

import com.fiveguysburger.emodiary.core.entity.NotificationTemplate

interface NotificationTemplateService {
    fun createTemplate(template: NotificationTemplate): NotificationTemplate
    fun getTemplateByType(notificationType: Int): NotificationTemplate?
    fun getAllActiveTemplates(): List<NotificationTemplate>
    fun updateTemplate(id: Long, template: NotificationTemplate): NotificationTemplate
    fun deactivateTemplate(id: Long)
} 