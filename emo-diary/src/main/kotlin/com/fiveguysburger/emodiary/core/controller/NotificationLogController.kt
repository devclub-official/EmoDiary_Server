@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.fiveguysburger.emodiary.core.controller

import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.service.NotificationLogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/notification-logs")
@Tag(name = "알림 로그", description = "알림 로그 관리 API")
class NotificationLogController(
    private val notificationLogService: NotificationLogService,
) {
    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 알림 로그 조회", description = "특정 상태의 알림 로그를 조회합니다.")
    fun findByNotificationStatus(
        @PathVariable status: NotificationStatus,
    ): ResponseEntity<List<NotificationLog>> = ResponseEntity.ok(notificationLogService.findByNotificationStatus(status))

    @GetMapping("/inactive-users")
    @Operation(summary = "장기 미접속 사용자 조회", description = "일주일 이상 로그인하지 않은 사용자 목록을 조회합니다.")
    fun findInactiveUsers(): ResponseEntity<List<Int>> = ResponseEntity.ok(notificationLogService.findInactiveUsers())

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자별 알림 이력 조회", description = "특정 사용자의 알림 이력을 조회합니다.")
    fun findUserNotificationHistory(
        @PathVariable userId: Int,
    ): ResponseEntity<List<NotificationLog>> = ResponseEntity.ok(notificationLogService.findUserNotificationHistory(userId))
}
