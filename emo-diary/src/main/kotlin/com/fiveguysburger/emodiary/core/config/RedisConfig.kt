package com.fiveguysburger.emodiary.core.config

import io.lettuce.core.ClientOptions
import io.lettuce.core.SocketOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig {
    @Value("\${spring.redis.host}")
    private lateinit var host: String

    @Value("\${spring.redis.port}")
    private var port: Int = 0

    @Value("\${spring.redis.password:}")
    private var password: String = "" // 기본값을 빈 문자열로 설정하고 lateinit 제거

    @Value("\${spring.redis.database}")
    private var database: Int = 0

    @Value("\${spring.redis.timeout}")
    private var timeout: Long = 60000

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        val redisConfig =
            RedisStandaloneConfiguration().apply {
                hostName = host
                this.port = port
                this.password = RedisPassword.of(password.toString())
                this.database = database
            }

        val clientOptions =
            ClientOptions
                .builder()
                .socketOptions(SocketOptions.builder().connectTimeout(Duration.ofMillis(timeout)).build())
                .build()

        val clientConfig =
            LettuceClientConfiguration
                .builder()
                .clientOptions(clientOptions)
                .commandTimeout(Duration.ofMillis(timeout))
                .build()

        return LettuceConnectionFactory(redisConfig, clientConfig)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, String> =
        RedisTemplate<String, String>().apply {
            connectionFactory = redisConnectionFactory()
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = StringRedisSerializer()
            afterPropertiesSet()
        }
}
