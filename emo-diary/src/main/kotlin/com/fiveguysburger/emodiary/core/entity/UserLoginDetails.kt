package com.fiveguysburger.emodiary.core.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import java.time.LocalDateTime

@Entity
@Table(name = "user_login_details")
class UserLoginDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private var id: Int? = null

    @Column(name = "user_id", nullable = false)
    var userId: Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    var user: Users? = null

    @Column(name = "login_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var loginAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "login_method")
    var loginMethod: String? = null

    @Column(name = "provider_id", nullable = false)
    var providerId: String? = null
}
