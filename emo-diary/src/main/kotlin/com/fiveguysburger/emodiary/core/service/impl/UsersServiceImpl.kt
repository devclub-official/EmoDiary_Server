package com.fiveguysburger.emodiary.core.service.impl

import com.fiveguysburger.emodiary.core.entity.QUserLoginDetails.userLoginDetails
import com.fiveguysburger.emodiary.core.entity.UserLoginDetails
import com.fiveguysburger.emodiary.core.entity.Users
import com.fiveguysburger.emodiary.core.repository.UsersLoginDetailsRepository
import com.fiveguysburger.emodiary.core.repository.UsersRepository
import com.fiveguysburger.emodiary.core.service.UsersService
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UsersServiceImpl(
    private val usersRepository: UsersRepository,
    private val userLoginDetailsRepository: UsersLoginDetailsRepository,
    private val queryFactory: JPAQueryFactory,
) : UsersService {
    override fun registerUser(
        email: String,
        providerId: String,
        loginMethod: String,
    ): Users {
        // 이미 존재하는 이메일인지 확인
        usersRepository.findByEmail(email)?.let {
            throw IllegalArgumentException("이미 존재하는 이메일입니다: $email")
        }

        // 새 사용자 생성
        val newUser =
            Users().apply {
                this.email = email
                this.alarmStatus = "N" // 기본값으로 알람 비활성화
            }

        val savedUser = usersRepository.save(newUser)

        // 첫 로그인 이력 생성
        updateLoginTime(savedUser.id!!, loginMethod, providerId)

        return savedUser
    }

    @Transactional(readOnly = true)
    override fun findUserByEmail(email: String): Users? = usersRepository.findByEmail(email)

    override fun updateLoginTime(
        userId: Int,
        loginMethod: String,
        providerId: String,
    ): UserLoginDetails {
        // 사용자 존재 확인
        val user =
            findUserById(userId)
                ?: throw IllegalArgumentException("존재하지 않는 사용자입니다: $userId")

        // 기존 로그인 이력 조회
        val existingLoginDetails = userLoginDetailsRepository.findByUserId(userId)

        return if (existingLoginDetails != null) {
            // 기존 레코드가 있으면 업데이트
            existingLoginDetails.apply {
                this.loginMethod = loginMethod
                this.providerId = providerId
                this.loginAt = LocalDateTime.now() // 최신 로그인 시간으로 업데이트
            }
            userLoginDetailsRepository.save(existingLoginDetails)
        } else {
            // 기존 레코드가 없으면 새로 생성
            val loginDetails =
                UserLoginDetails().apply {
                    this.userId = userId
                    this.loginMethod = loginMethod
                    this.providerId = providerId
                    this.loginAt = LocalDateTime.now()
                }
            userLoginDetailsRepository.save(loginDetails)
        }
    }

    override fun updateAlarmStatus(
        userId: Int,
        alarmStatus: String,
    ): Users {
        val user =
            findUserById(userId)
                ?: throw IllegalArgumentException("존재하지 않는 사용자입니다: $userId")

        user.alarmStatus = alarmStatus
        user.updatedAt = LocalDateTime.now()

        return usersRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun findUserById(userId: Int): Users? = usersRepository.findById(userId).orElse(null)

    @Transactional(readOnly = true)
    override fun findInactiveUserIds(days: Int): List<Int> {
        val cutoffDate = LocalDateTime.now().minusDays(days.toLong())
        return queryFactory
            .select(userLoginDetails.userId)
            .from(userLoginDetails)
            .where(userLoginDetails.loginAt.lt(cutoffDate))
            .fetch()
            .mapNotNull { it }
    }
}
