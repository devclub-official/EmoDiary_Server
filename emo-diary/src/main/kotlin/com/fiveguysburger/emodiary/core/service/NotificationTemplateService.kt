package com.fiveguysburger.emodiary.core.service

import com.fiveguysburger.emodiary.core.dto.NotificationTemplateRequestDto
import com.fiveguysburger.emodiary.core.dto.NotificationTemplateResponseDto
import com.fiveguysburger.emodiary.core.entity.NotificationTemplate

interface NotificationTemplateService {
    fun createTemplate(request: NotificationTemplateRequestDto): NotificationTemplateResponseDto

    fun getTemplate(id: Long): NotificationTemplateResponseDto

    fun getTemplateByType(notificationType: Int): NotificationTemplate?

    fun getAllActiveTemplates(): List<NotificationTemplate>

    fun updateTemplate(
        id: Long,
        request: NotificationTemplateRequestDto,
    ): NotificationTemplateResponseDto

    fun deactivateTemplate(id: Long)
}
