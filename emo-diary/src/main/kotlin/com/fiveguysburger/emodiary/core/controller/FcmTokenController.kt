package com.fiveguysburger.emodiary.core.controller

import com.fiveguysburger.emodiary.core.dto.ApiResponse
import com.fiveguysburger.emodiary.core.dto.request.FcmTokenRequest
import com.fiveguysburger.emodiary.core.service.FcmTokenService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/fcm")
@Tag(name = "FCM 토큰", description = "FCM 토큰 관리 API")
class FcmTokenController(
    private val fcmTokenService: FcmTokenService,
) {
    @PostMapping("/token")
    fun saveFcmToken(
        @RequestBody request: FcmTokenRequest,
    ): ApiResponse<Unit> {
        fcmTokenService.saveFcmToken(request)
        val savedToken = fcmTokenService.getFcmToken(request.userId)
        return if (savedToken != null) {
            ApiResponse.success(message = "FCM 토큰이 성공적으로 저장되었습니다.")
        } else {
            ApiResponse.error(message = "FCM 토큰 저장에 실패했습니다.")
        }
    }

    @DeleteMapping("/token/{userId}")
    fun deleteFcmToken(
        @PathVariable userId: Int,
    ): ApiResponse<Unit> {
        val tokenBeforeDelete = fcmTokenService.getFcmToken(userId)
        fcmTokenService.deleteFcmToken(userId)
        val tokenAfterDelete = fcmTokenService.getFcmToken(userId)
        return if (tokenBeforeDelete != null && tokenAfterDelete == null) {
            ApiResponse.success(message = "FCM 토큰이 성공적으로 삭제되었습니다.")
        } else {
            ApiResponse.error(message = "FCM 토큰 삭제에 실패했습니다.")
        }
    }

    @GetMapping("/token/{userId}")
    fun getFcmToken(
        @PathVariable userId: Int,
    ): ApiResponse<String?> {
        val token = fcmTokenService.getFcmToken(userId)
        return if (token != null) {
            ApiResponse.success(data = token, message = "FCM 토큰이 존재합니다.")
        } else {
            ApiResponse.success(data = null, message = "FCM 토큰이 존재하지 않습니다.")
        }
    }
}
