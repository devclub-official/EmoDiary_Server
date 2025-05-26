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
}
