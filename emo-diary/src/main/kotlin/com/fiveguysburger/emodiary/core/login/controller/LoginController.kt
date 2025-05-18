package com.fiveguysburger.emodiary.core.login.controller

import com.fiveguysburger.emodiary.core.login.service.LoginService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
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
                description = "로그인 성공1",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                    ),
                ],
            ),
        ],
    )
    @GetMapping("/google")
    fun googleLogin(
        @RequestParam code: String,
    ) {
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
    @GetMapping("/google")
    fun kakaoLogin(
        @RequestParam code: String,
    ) {
    }
}
