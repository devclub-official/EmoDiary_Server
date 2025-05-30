package com.fiveguysburger.emodiary.core.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notification_templates")
data class NotificationTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "notification_type", nullable = false, unique = true)
    val notificationType: Int,
    @Column(nullable = false)
    val title: String,
    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "is_active")
    val isActive: Boolean = true,
)
