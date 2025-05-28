package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.entity.NotificationTemplate
import com.fiveguysburger.emodiary.core.repository.NotificationTemplateRepository
import com.fiveguysburger.emodiary.core.service.NotificationTemplateService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class NotificationTemplateServiceImpl(
    private val notificationTemplateRepository: NotificationTemplateRepository,
) : NotificationTemplateService {
    override fun createTemplate(template: NotificationTemplate): NotificationTemplate = notificationTemplateRepository.save(template)

    override fun getTemplateByType(notificationType: Int): NotificationTemplate? =
        notificationTemplateRepository.findByNotificationType(notificationType)

    override fun getAllActiveTemplates(): List<NotificationTemplate> = notificationTemplateRepository.findAllActive()

    override fun updateTemplate(
        id: Long,
        template: NotificationTemplate,
    ): NotificationTemplate {
        val existingTemplate =
            notificationTemplateRepository
                .findById(id)
                .orElseThrow { NoSuchElementException("Template Id 값으로 조회할 수 없습니다: $id") }

        return notificationTemplateRepository.save(
            existingTemplate.copy(
                title = template.title,
                content = template.content,
                notificationType = template.notificationType,
            ),
        )
    }

    override fun deactivateTemplate(id: Long) {
        val template =
            notificationTemplateRepository
                .findById(id)
                .orElseThrow { NoSuchElementException("Template Id 값으로 조회할 수 없습니다: $id") }

        notificationTemplateRepository.save(template.copy(isActive = false))
    }
} 
