package com.fiveguysburger.emodiary.core.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    @Value("\${rabbitmq.notification.exchange}")
    private lateinit var notificationExchange: String

    @Value("\${rabbitmq.notification.queue}")
    private lateinit var notificationQueue: String

    @Value("\${rabbitmq.notification.routing-key}")
    private lateinit var notificationRoutingKey: String

    @Bean
    fun notificationExchange(): DirectExchange = DirectExchange(notificationExchange)

    @Bean
    fun notificationQueue(): Queue =
        // 큐 이름
        // durable 서버 재시작 후에도 큐 유지
        // exclusive 여러 연결에서 큐 접근 가능
        // autoDelete 마지막 소비자 연결 해제 시에도 큐 유지
        Queue(
            notificationQueue,
            true,
            false,
            false,
        )

    @Bean
    fun notificationBinding(): Binding =
        BindingBuilder
            .bind(notificationQueue())
            .to(notificationExchange())
            .with(notificationRoutingKey)

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
        return rabbitTemplate
    }
}
