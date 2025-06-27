package com.fiveguysburger.emodiary.core.repository

import com.fiveguysburger.emodiary.core.entity.NotificationLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationLogRepository :
    JpaRepository<NotificationLog, String>,
    NotificationLogRepositoryCustom
