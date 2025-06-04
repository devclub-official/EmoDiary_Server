@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.fiveguysburger.emodiary.core.controller

import com.fiveguysburger.emodiary.core.dto.NotificationTemplateRequestDto
import com.fiveguysburger.emodiary.core.dto.NotificationTemplateResponseDto
import com.fiveguysburger.emodiary.core.entity.NotificationTemplate
import com.fiveguysburger.emodiary.core.service.NotificationTemplateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "알림 템플릿", description = "알림 템플릿 관리 API")
@RestController
@RequestMapping("/api/v1/notification-templates")
class NotificationTemplateController(
    private val notificationTemplateService: NotificationTemplateService,
) {
    @Operation(
        summary = "알림 템플릿 생성",
        description = "새로운 알림 템플릿을 생성합니다.",
    )
    @PostMapping
    fun createTemplate(
        @RequestBody request: NotificationTemplateRequestDto,
    ): ResponseEntity<NotificationTemplateResponseDto> = ResponseEntity.ok(notificationTemplateService.createTemplate(request))

    @Operation(
        summary = "활성화된 템플릿 목록 조회",
        description = "현재 활성화된 모든 알림 템플릿을 조회합니다.",
    )
    @GetMapping
    fun getAllActiveTemplates(): ResponseEntity<List<NotificationTemplate>> =
        ResponseEntity.ok(notificationTemplateService.getAllActiveTemplates())

    @Operation(
        summary = "알림 템플릿 조회",
        description = "템플릿 ID로 템플릿을 조회합니다.",
    )
    @GetMapping("/{id}")
    fun getTemplate(
        @PathVariable id: Long,
    ): ResponseEntity<NotificationTemplateResponseDto> = ResponseEntity.ok(notificationTemplateService.getTemplate(id))

    @Operation(
        summary = "알림 템플릿 수정",
        description = "기존 알림 템플릿을 수정합니다.",
    )
    @PutMapping("/{id}")
    fun updateTemplate(
        @PathVariable id: Long,
        @RequestBody request: NotificationTemplateRequestDto,
    ): ResponseEntity<NotificationTemplateResponseDto> =
        ResponseEntity.ok(notificationTemplateService.updateTemplate(id, request))

    @Operation(
        summary = "알림 템플릿 비활성화",
        description = "알림 템플릿을 비활성화합니다.",
    )
    @DeleteMapping("/{id}")
    fun deactivateTemplate(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        notificationTemplateService.deactivateTemplate(id)
        return ResponseEntity.noContent().build()
    }
} 
