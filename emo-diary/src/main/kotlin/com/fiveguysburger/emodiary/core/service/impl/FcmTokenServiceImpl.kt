package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.constant.FcmConstants
import com.fiveguysburger.emodiary.core.dto.request.FcmTokenRequest
import com.fiveguysburger.emodiary.core.service.FcmTokenService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class FcmTokenServiceImpl(
    private val redisTemplate: RedisTemplate<String, String>,
) : FcmTokenService {
    override fun saveFcmToken(request: FcmTokenRequest) {
        val key = FcmConstants.FCM_TOKEN_KEY_PREFIX + request.userId
        redisTemplate.opsForValue().set(key, request.fcmToken, Duration.ofDays(FcmConstants.FCM_TOKEN_EXPIRY_DAYS))
    }

    override fun deleteFcmToken(userId: Int) {
        val key = FcmConstants.FCM_TOKEN_KEY_PREFIX + userId
        redisTemplate.delete(key)
    }

    override fun getFcmToken(userId: Int): String? {
        val key = FcmConstants.FCM_TOKEN_KEY_PREFIX + userId
        return redisTemplate.opsForValue().get(key)
    }
}
