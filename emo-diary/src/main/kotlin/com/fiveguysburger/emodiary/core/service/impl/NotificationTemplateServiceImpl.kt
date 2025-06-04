package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.dto.NotificationTemplateRequestDto
import com.fiveguysburger.emodiary.core.dto.NotificationTemplateResponseDto
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
    override fun createTemplate(request: NotificationTemplateRequestDto): NotificationTemplateResponseDto {
        val template = NotificationTemplate(
            notificationType = request.notificationType,
            title = request.title,
            content = request.content,
        )
        return notificationTemplateRepository.save(template).toResponseDto()
    }

    override fun getTemplate(id: Long): NotificationTemplateResponseDto {
        val template = notificationTemplateRepository.findById(id)
            .orElseThrow { NoSuchElementException("템플릿을 찾을 수 없습니다: $id") }
        return template.toResponseDto()
    }

    override fun getTemplateByType(notificationType: Int): NotificationTemplate? =
        notificationTemplateRepository.findByNotificationType(notificationType)

    override fun getAllActiveTemplates(): List<NotificationTemplate> =
        notificationTemplateRepository.findAllActive()

    override fun updateTemplate(
        id: Long,
        request: NotificationTemplateRequestDto,
    ): NotificationTemplateResponseDto {
        val existingTemplate = notificationTemplateRepository.findById(id)
            .orElseThrow { NoSuchElementException("템플릿을 찾을 수 없습니다: $id") }

        return notificationTemplateRepository.save(
            existingTemplate.copy(
                title = request.title,
                content = request.content,
            ),
        ).toResponseDto()
    }

    override fun deactivateTemplate(id: Long) {
        val template = notificationTemplateRepository.findById(id)
            .orElseThrow { NoSuchElementException("템플릿을 찾을 수 없습니다: $id") }

        notificationTemplateRepository.save(template.copy(isActive = false))
    }

    private fun NotificationTemplate.toResponseDto() = NotificationTemplateResponseDto(
        id = id,
        notificationType = notificationType,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isActive = isActive,
    )
}
