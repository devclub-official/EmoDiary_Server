package com.fiveguysburger.emodiary.core.repository

import com.fiveguysburger.emodiary.core.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository : JpaRepository<Users, Int> {
    fun findByEmail(email: String): Users?
}
