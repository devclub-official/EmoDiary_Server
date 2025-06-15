package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.dto.NotificationMessageDto
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class NotificationProducer(
    private val rabbitTemplate: RabbitTemplate,
) {
    @Value("\${rabbitmq.notification.exchange}")
    private lateinit var exchange: String

    @Value("\${rabbitmq.notification.routing-key}")
    private lateinit var routingKey: String

    fun sendNotification(notificationMessage: NotificationMessageDto) {
        rabbitTemplate.convertAndSend(exchange, routingKey, notificationMessage)
    }
}
