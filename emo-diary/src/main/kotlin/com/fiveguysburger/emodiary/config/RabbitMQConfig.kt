@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.fiveguysburger.emodiary.config

import org.springframework.amqp.core.*
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
    fun notificationQueue(): Queue = Queue(notificationQueue)

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
