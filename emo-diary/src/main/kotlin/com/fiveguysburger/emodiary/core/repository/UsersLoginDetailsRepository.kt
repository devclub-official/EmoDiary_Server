package com.fiveguysburger.emodiary.core.repository

import com.fiveguysburger.emodiary.core.entity.UserLoginDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersLoginDetailsRepository : JpaRepository<UserLoginDetails, Int> {
    fun findByUserId(userId: Int): UserLoginDetails?
}
