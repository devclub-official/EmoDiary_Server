package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.dto.LoginHistoryResponseDto
import com.fiveguysburger.emodiary.core.repository.UsersLoginDetailsRepository
import com.fiveguysburger.emodiary.core.repository.UsersRepository
import com.fiveguysburger.emodiary.core.service.MypageService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MypageServiceImpl(
    private val usersRepository: UsersRepository,
    private val userLoginDetailsRepository: UsersLoginDetailsRepository,
) : MypageService {
    override fun findUserLoginHistoryById(userId: Int): LoginHistoryResponseDto? =
        userLoginDetailsRepository.findByUserId(userId)?.let { entity ->
            LoginHistoryResponseDto(
                loginAt = entity.loginAt,
                loginMethod = entity.loginMethod,
            )
        }

    @Transactional
    override fun deleteUserById(userId: Int): Boolean {
        try {
            var isDeletedSuccess = false
            val loginDetailsDeleted = userLoginDetailsRepository.deleteUserLoginDetailByUserId(userId)

            usersRepository.deleteById(userId)
            if (loginDetailsDeleted > 0) {
                isDeletedSuccess = true
            }

            return isDeletedSuccess
        } catch (e: Exception) {
            System.out.println(e.printStackTrace())
            return false
        }
    }

    @Transactional
    override fun updateAlarmStatus(
        userId: Int,
        alarmStatus: String,
    ): Boolean {
        // alarmStatus 검증
        if (alarmStatus != "Y" && alarmStatus != "N") {
            throw IllegalArgumentException("alarmStatus는 'Y' 또는 'N'만 가능합니다. 입력값: $alarmStatus")
        }

        val user = usersRepository.findById(userId).orElse(null)
        return if (user != null) {
            user.alarmStatus = alarmStatus // 직접 String 할당
            usersRepository.save(user)
            true
        } else {
            false
        }
    }
}
