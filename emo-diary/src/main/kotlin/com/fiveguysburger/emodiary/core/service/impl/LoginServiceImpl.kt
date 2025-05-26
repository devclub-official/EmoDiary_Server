package com.fiveguysburger.emodiary.core.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fiveguysburger.emodiary.core.dto.TokenResponse
import com.fiveguysburger.emodiary.core.entity.UserLoginDetails
import com.fiveguysburger.emodiary.core.entity.Users
import com.fiveguysburger.emodiary.core.repository.UsersLoginDetailsRepository
import com.fiveguysburger.emodiary.core.repository.UsersRepository
import com.fiveguysburger.emodiary.core.service.LoginService
import com.fiveguysburger.emodiary.core.service.UsersService
import com.fiveguysburger.emodiary.util.JwtTokenUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class LoginServiceImpl(
    private val webClient: WebClient,
    private val jwtTokenUtil: JwtTokenUtil,
    private val objectMapper: ObjectMapper,
    private val usersService: UsersService,
    private val userRepository: UsersRepository,
    private val userLoginDetailsRepository: UsersLoginDetailsRepository,
) : LoginService {
    private val log = LoggerFactory.getLogger(this::class.java)

    // Google OAuth2 설정값 추가
    @Value("\${spring.security.oauth2.client.registration.google.client-id}")
    private lateinit var googleClientId: String

    @Value("\${spring.security.oauth2.client.registration.google.client-secret}")
    private lateinit var googleClientSecret: String

    @Value("\${spring.security.oauth2.client.registration.google.redirect-uri}")
    private lateinit var googleRedirectUri: String

    // Google OAuth2 엔드포인트
    @Value("\${spring.security.oauth2.client.provider.google.token-uri}")
    private lateinit var googleTokenUri: String

    @Value("\${spring.security.oauth2.client.provider.google.user-info-uri}")
    private lateinit var googleUserInfoUri: String

    @Value("\${spring.security.oauth2.client.registration.kakao.client-id}")
    private lateinit var kakaoClientId: String

    @Value("\${spring.security.oauth2.client.registration.kakao.client-secret}")
    private lateinit var kakaoClientSecret: String

    @Value("\${spring.security.oauth2.client.provider.kakao.token-uri}")
    private lateinit var kakaoTokenUri: String

    @Value("\${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private lateinit var kakaoUserInfoUri: String

    @Value("\${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private lateinit var kakaoRedirectUri: String

    override fun loginWithGoogle(code: String): TokenResponse {
        val tokenResponse =
            getGoogleAccessToken(code).block()
                ?: throw RuntimeException("Failed to get access token from Google")

        log.debug("Google Token Response: $tokenResponse")
        val accessToken = tokenResponse["access_token"] as String

        // 2. 액세스 토큰으로 사용자 정보 요청
        val userInfo =
            getGoogleUserInfo(accessToken).block()
                ?: throw RuntimeException("Failed to get user info from Google")

        // 3. 사용자 정보 추출
        val googleId = userInfo["sub"] as String
        val email = userInfo["email"] as String

        // 4. 사용자 확인 및 처리
        val user = processUserLogin(email, googleId, "google")

        // 4. JWT 토큰 쌍(액세스 토큰, 리프레시 토큰) 생성
        val (jwtAccessToken, jwtRefreshToken) = jwtTokenUtil.generateTokenPair(googleId, email)

        // 5. TokenResponse 객체로 변환하여 반환
        return TokenResponse.of(
            accessToken = jwtAccessToken,
            refreshToken = jwtRefreshToken,
            expiresIn = jwtTokenUtil.jwtExpirationMs / 1000, // 밀리초를 초로 변환
        )
    }

    private fun getGoogleAccessToken(code: String): Mono<Map<String, Any>> {
        val params =
            LinkedMultiValueMap<String, String>().apply {
                add("client_id", googleClientId)
                add("client_secret", googleClientSecret)
                add("code", code)
                add("grant_type", "authorization_code")
                add("redirect_uri", googleRedirectUri)
            }

        return webClient
            .post()
            .uri(googleTokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(params))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<String, Any>>() {})
            .doOnError { error ->
                throw RuntimeException("Failed to get Google access token", error)
            }
    }

    private fun getGoogleUserInfo(accessToken: String): Mono<Map<String, Any>> =
        webClient
            .get()
            .uri(googleUserInfoUri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .bodyToMono(String::class.java)
            .map { responseBody ->
                objectMapper.readValue(responseBody, object : TypeReference<Map<String, Any>>() {})
            }.onErrorMap { error ->
                log.error("Error getting Google user info: ${error.message}", error)
                RuntimeException("Failed to get Google user info", error)
            }

    override fun loginWithKakao(code: String): TokenResponse {
        val tokenResponse =
            getKakaoAccessToken(code).block()
                ?: throw RuntimeException("Failed to get access token from Kakao")

        log.debug("Kakao Token Response: $tokenResponse")
        val accessToken = tokenResponse["access_token"] as String

        // 2. 액세스 토큰으로 사용자 정보 요청
        val userInfo =
            getKakaoUserInfo(accessToken).block()
                ?: throw RuntimeException("Failed to get user info from Kakao")

        log.debug("Kakao User Info: $userInfo") // 디버깅용 로그 추가

        // 3. 사용자 정보 추출 (Null 체크 추가)
        val kakaoId = userInfo["id"].toString()
        val kakaoAccount =
            userInfo["kakao_account"] as? Map<String, Any>
                ?: throw RuntimeException("kakao_account 정보를 가져올 수 없습니다")

        // nickname을 email로 사용
        val profileMap =
            kakaoAccount["profile"] as? Map<String, Any>
                ?: throw RuntimeException("카카오 계정에서 프로필 정보를 가져올 수 없습니다.")

        val nickname =
            profileMap["nickname"] as? String
                ?: throw RuntimeException("카카오 계정에서 닉네임 정보를 가져올 수 없습니다.")
        // 4. 사용자 확인 및 처리
        val user = processUserLogin(nickname, kakaoId, "kakao")

        // 5. JWT 토큰 쌍(액세스 토큰, 리프레시 토큰) 생성
        val (jwtAccessToken, jwtRefreshToken) = jwtTokenUtil.generateTokenPair(kakaoId, nickname)

        // 6. TokenResponse 객체로 변환하여 반환
        return TokenResponse.of(
            accessToken = jwtAccessToken,
            refreshToken = jwtRefreshToken,
            expiresIn = jwtTokenUtil.jwtExpirationMs / 1000,
        )
    }

    // Kakao 액세스 토큰 요청
    private fun getKakaoAccessToken(code: String): Mono<Map<String, Any>> {
        val params =
            LinkedMultiValueMap<String, String>().apply {
                add("grant_type", "authorization_code")
                add("client_id", kakaoClientId)
                add("client_secret", kakaoClientSecret)
                add("redirect_uri", kakaoRedirectUri)
                add("code", code)
            }

        return webClient
            .post()
            .uri(kakaoTokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(params))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<String, Any>>() {})
            .doOnError { error ->
                log.error("Failed to get Kakao access token: ${error.message}", error)
                throw RuntimeException("Failed to get Kakao access token", error)
            }
    }

    // Kakao 사용자 정보 요청
    private fun getKakaoUserInfo(accessToken: String): Mono<Map<String, Any>> =
        webClient
            .get()
            .uri(kakaoUserInfoUri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .retrieve()
            .bodyToMono(String::class.java)
            .map { responseBody ->
                objectMapper.readValue(responseBody, object : TypeReference<Map<String, Any>>() {})
            }.onErrorMap { error ->
                log.error("Error getting Kakao user info: ${error.message}", error)
                RuntimeException("Failed to get Kakao user info", error)
            }

    @Transactional
    fun saveUserLogin(
        email: String,
        providerId: String,
        loginMethod: String,
    ): Users {
        val user =
            userRepository.findByEmail(email) ?: run {
                val newUser =
                    Users().apply {
                        this.email = email
                        this.alarmStatus = false
                    }
                userRepository.save(newUser)
            }

        // 2. 로그인 정보 저장
        val loginDetails =
            UserLoginDetails().apply {
                this.userId = user.id
                this.providerId = providerId
                this.loginMethod = loginMethod
            }
        userLoginDetailsRepository.save(loginDetails)

        return user
    }

    private fun processUserLogin(
        email: String,
        providerId: String,
        loginMethod: String,
    ): Users =
        try {
            // 이메일로 기존 사용자 확인
            val existingUser = usersService.findUserByEmail(email)

            if (existingUser != null) {
                // 기존 사용자인 경우: 로그인 시간 업데이트
                log.info("기존 사용자 로그인: email=$email, userId=${existingUser.id}")
                usersService.updateLoginTime(
                    userId = existingUser.id!!,
                    loginMethod = loginMethod,
                    providerId = providerId,
                )
                existingUser
            } else {
                // 신규 사용자인 경우: 회원가입 처리
                log.info("신규 사용자 가입: email=$email, providerId=$providerId")
                usersService.registerUser(
                    email = email,
                    providerId = providerId,
                    loginMethod = loginMethod,
                )
            }
        } catch (e: Exception) {
            log.error("사용자 로그인 처리 중 오류 발생: email=$email", e)
            throw RuntimeException("사용자 로그인 처리에 실패했습니다: ${e.message}", e)
        }
}
