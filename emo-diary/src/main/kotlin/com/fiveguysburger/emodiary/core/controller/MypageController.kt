package com.fiveguysburger.emodiary.core.controller

import com.fiveguysburger.emodiary.core.dto.LoginHistoryResponseDto
import com.fiveguysburger.emodiary.core.service.MypageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/mypages")
@Tag(name = "마이페이지", description = "마이페이지 API")
class MypageController(
    private val mypageService: MypageService,
) {
    // 1. 로그인 시간 및 로그인 종류를 조회한다
    @Operation(
        summary = "로그인 이력 조회",
        description = "사용자의 로그인 시간 및 로그인 제공자(Google/Kakao)를 조회합니다",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "로그인 이력 조회 성공"),
        ],
    )
    @GetMapping("/login-history")
    fun findLoginHistory(
        @Parameter(description = "사용자 ID", required = true)
        @RequestParam userId: Int,
    ): ResponseEntity<LoginHistoryResponseDto> = ResponseEntity.ok(mypageService.findUserLoginHistoryById(userId))

    // 3. 회원 탈퇴 가능
    @Operation(
        summary = "회원 탈퇴 처리",
        description = "사용자 계정을 탈퇴 처리하고 개인정보를 안전하게 삭제합니다",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "회원 탈퇴 처리 완료"),
        ],
    )
    @DeleteMapping("/withdraw")
    fun withdrawAccount(
        @Parameter(description = "사용자 ID", required = true)
        @RequestParam userId: Int,
    ): ResponseEntity<Boolean> =
        try {
            val result = mypageService.deleteUserById(userId)

            if (result) {
                ResponseEntity.ok(true)
            } else {
                ResponseEntity.badRequest().body(false)
            }
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(false)
        }

    // 4. 알람 설정 토글
    @Operation(
        summary = "알람 설정 토글",
        description = "특정 알람 타입의 활성화/비활성화 상태를 전환합니다",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "알람 설정 변경 성공"),
        ],
    )
    @PutMapping("/alarm-status")
    fun toggleAlarmSetting(
        @Parameter(description = "사용자 ID", required = true)
        @RequestParam userId: Int,
        @RequestParam alarmStatus: String,
    ): ResponseEntity<Boolean> {
        val isAlarmToggleSuccess = mypageService.updateAlarmStatus(userId, alarmStatus)

        return ResponseEntity.ok(isAlarmToggleSuccess)
    }
}
