package com.fiveguysburger.emodiary.core.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class DuplicateMessageService(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    private val duplicateCheckExpiration = Duration.ofMinutes(5) // 5분간 중복 체크

    /**
     * 메시지 ID가 중복인지 확인합니다.
     * @param messageId 확인할 메시지 ID
     * @return 중복이면 true, 아니면 false
     */
    fun isDuplicate(messageId: String): Boolean {
        val key = "notification:duplicate:$messageId"
        val exists = redisTemplate.hasKey(key)
        if (!exists) {
            redisTemplate.opsForValue().set(key, "1", duplicateCheckExpiration)
        }
        return exists
    }

    /**
     * 중복 체크 키를 수동으로 삭제합니다 (테스트용).
     * @param messageId 삭제할 메시지 ID
     */
    fun removeDuplicateCheck(messageId: String) {
        val key = "notification:duplicate:$messageId"
        redisTemplate.delete(key)
    }
}
