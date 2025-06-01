package com.fiveguysburger.emodiary.core.controller

import com.fiveguysburger.emodiary.core.dto.TokenResponse
import com.fiveguysburger.emodiary.core.service.LoginService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/login")
class LoginController(
    private val loginService: LoginService,
) {
    // Google SNS 로그인
    @Operation(
        summary = "구글 로그인 콜백",
        description = "구글 로그인 인증 코드로 사용자 인증 및 JWT 토큰 발급",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = TokenResponse::class),
                    ),
                ],
            ),
        ],
    )
    @GetMapping("/google")
    fun googleLogin(
        @RequestParam code: String,
    ): ResponseEntity<TokenResponse> {
        val token = loginService.loginWithGoogle(code)
        return ResponseEntity.ok(token)
    }

    @Operation(
        summary = "카카오 로그인 콜백",
        description = "카카오 로그인 인증 코드로 사용자 인증 및 JWT 토큰 발급",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                    ),
                ],
            ),
        ],
    )
    @GetMapping("/kakao")
    fun kakaoLogin(
        @RequestParam code: String,
    ): ResponseEntity<TokenResponse> {
        val token = loginService.loginWithKakao(code)
        return ResponseEntity.ok(token)
    }

    @PostMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") authorization: String,
    ): ResponseEntity<Map<String, Any>> {
        return try {
            // Bearer 토큰에서 실제 토큰 추출
            if (!authorization.startsWith("Bearer ")) {
                return ResponseEntity
                    .status(401)
                    .body(mapOf("error" to "Authorization 헤더 형식이 올바르지 않습니다."))
            }

            val token = authorization.removePrefix("Bearer ").trim()

            if (token.isEmpty()) {
                return ResponseEntity
                    .status(401)
                    .body(mapOf("error" to "토큰이 제공되지 않았습니다."))
            }

            val success = loginService.logout(token)

            if (success) {
                ResponseEntity.ok(
                    mapOf(
                        "message" to "로그아웃이 완료되었습니다.",
                        "timestamp" to System.currentTimeMillis(),
                    ),
                )
            } else {
                ResponseEntity.badRequest().body(
                    mapOf(
                        "error" to "로그아웃에 실패했습니다.",
                        "timestamp" to System.currentTimeMillis(),
                    ),
                )
            }
        } catch (e: Exception) {
            ResponseEntity.status(401).body(
                mapOf(
                    "error" to "유효하지 않은 토큰입니다.",
                    "timestamp" to System.currentTimeMillis(),
                ),
            )
        }
    }
}
