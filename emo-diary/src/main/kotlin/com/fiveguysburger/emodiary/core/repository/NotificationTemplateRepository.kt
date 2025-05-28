package com.fiveguysburger.emodiary.core.repository

import com.fiveguysburger.emodiary.core.entity.NotificationTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationTemplateRepository :
    JpaRepository<NotificationTemplate, Long>,
    NotificationTemplateRepositoryCustom
