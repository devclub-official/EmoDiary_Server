package com.fiveguysburger.emodiary.core.repository

import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.entity.NotificationLogId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationLogRepository :
    JpaRepository<NotificationLog, NotificationLogId>,
    NotificationLogRepositoryCustom {
    /**
     * 특정 사용자의 알림 이력을 생성일시 기준 내림차순으로 조회합니다.
     * @param userId 사용자 ID
     * @return 해당 사용자의 알림 로그 목록
     */
    override fun findByUserIdOrderByCreatedAtDesc(userId: Int): List<NotificationLog>
}
