package com.fiveguysburger.emodiary.core.entity

import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.enums.NotificationType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime
import java.util.Objects

@Entity
@Table(name = "notification_logs")
@IdClass(NotificationLogId::class)
data class NotificationLog(
    @Id
    @Column(name = "user_id")
    val userId: Int,
    @Id
    @Column(name = "sent_at")
    val sentAt: LocalDateTime,
    @Id
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

        return userId == other.userId && sentAt == other.sentAt && templateId == other.templateId
    }

    override fun hashCode(): Int = Objects.hash(userId, sentAt, templateId)

    @Override
    override fun toString(): String = this::class.simpleName + "(  userId = $userId   ,   sentAt = $sentAt   ,   templateId = $templateId )"
}

/**
 * 복합키 사용 시 아래와 같이 클래스를 추가로 정의해줘야 한다.
 */
data class NotificationLogId(
    val userId: Int,
    val sentAt: LocalDateTime,
    val templateId: Int,
) : java.io.Serializable
