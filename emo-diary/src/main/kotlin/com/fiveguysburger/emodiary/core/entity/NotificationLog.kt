@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.fiveguysburger.emodiary.core.entity

import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.enums.NotificationType
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID

@Entity
@Table(name = "notification_logs")
data class NotificationLog(
    @Id
    @Column(name = "id", length = 36)
    val id: String = UUID.randomUUID().toString(),
    @Column(name = "user_id")
    val userId: Int,
    @Column(name = "sent_at")
    val sentAt: LocalDateTime,
    @Column(name = "template_id")
    val templateId: Int,
    @Column(name = "notification_type")
    @Enumerated(EnumType.ORDINAL)
    val notificationType: NotificationType,
    @Column(name = "notification_status")
    @Enumerated(EnumType.ORDINAL)
    val notificationStatus: NotificationStatus = NotificationStatus.PENDING,
    @Column(name = "fcm_message_id")
    val fcmMessageId: String? = null,
    @Column(name = "error_message", columnDefinition = "TEXT")
    val errorMessage: String? = null,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass = if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass = this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as NotificationLog

        return id == other.id
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = this::class.simpleName + "(id = $id)"
}
