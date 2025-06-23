package com.fiveguysburger.emodiary.core.config.job

import com.fiveguysburger.emodiary.core.dto.FcmMessageDto
import com.fiveguysburger.emodiary.core.enums.NotificationType
import com.fiveguysburger.emodiary.core.service.FcmService
import com.fiveguysburger.emodiary.core.service.FcmTokenService
import com.fiveguysburger.emodiary.core.service.UsersService
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class InactiveUserNotifyJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val usersService: UsersService,
    private val fcmTokenService: FcmTokenService,
    private val fcmService: FcmService,
) {
    @Suppress("ktlint:standard:property-naming")
    private val CHUNK_SIZE = 100

    @Bean
    fun inactiveUserNotifyJob(): Job =
        JobBuilder("inactiveUserNotifyJob", jobRepository)
            .start(inactiveUserNotifyStep())
            .build()

    @Bean
    @JobScope
    fun inactiveUserNotifyStep(): Step =
        StepBuilder("inactiveUserNotifyStep", jobRepository)
            .chunk<Int, FcmMessageDto>(CHUNK_SIZE, transactionManager)
            .reader(inactiveUserReader())
            .processor(inactiveUserProcessor())
            .writer(inactiveUserWriter())
            .build()

    @Bean
    @StepScope
    fun inactiveUserReader(): ListItemReader<Int> {
        val inactiveUserIds = usersService.findInactiveUserIds(7)
        return ListItemReader(inactiveUserIds)
    }

    @Bean
    fun inactiveUserProcessor(): ItemProcessor<Int, FcmMessageDto> =
        ItemProcessor { userId ->
            val token = fcmTokenService.getFcmToken(userId)
            if (token != null) {
                FcmMessageDto(
                    userId = userId,
                    token = token,
                    notificationType = NotificationType.INACTIVE_DIARY,
                    data = mapOf("reason" to "inactive_user_notification"),
                )
            } else {
                null // 토큰 없는 사용자는 건너뛰기
            }
        }

    @Bean
    fun inactiveUserWriter(): ItemWriter<FcmMessageDto> =
        ItemWriter { chunk ->
            chunk.forEach { message ->
                fcmService.sendMessage(message)
            }
        }
}
