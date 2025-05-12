package com.fiveguysburger.emodiary.redis

import com.fiveguysburger.emodiary.core.EmoDiaryApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

@SpringBootTest(classes = [EmoDiaryApplication::class])
class RedisConnectionTest {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Test
    fun testRedisConnection() {
        // given
        val valueOperations = redisTemplate.opsForValue()
        val key = "testKey"
        val value = "testValue"

        // when
        valueOperations.set(key, value)
        val retrievedValue = valueOperations.get(key)

        // then
        assertThat(retrievedValue).isEqualTo(value)

        // cleanup
        redisTemplate.delete(key)
    }
}
