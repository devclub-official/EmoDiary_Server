package com.fiveguysburger.emodiary.core.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Supplier

@Configuration
class QuerydslConfig(
    private val entityManager: EntityManager,
) {
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {
        // Supplier 생성자 사용
        val supplier = Supplier<EntityManager> { entityManager }
        return JPAQueryFactory(supplier)
    }
}
