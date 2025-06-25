package com.fiveguysburger.emodiary.core.config.job

import com.fiveguysburger.emodiary.core.dto.FcmMessageDto
import com.fiveguysburger.emodiary.core.enums.NotificationType
import com.fiveguysburger.emodiary.core.service.FcmService
import com.fiveguysburger.emodiary.core.service.FcmTokenService
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.context.annotation.DependsOn

@Configuration
@DependsOn("fcmTokenServiceImpl", "fcmServiceImpl")
class DiaryReminderJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val fcmTokenService: FcmTokenService,
    private val fcmService: FcmService,
) {
    @Suppress("ktlint:standard:property-naming")
    private val CHUNK_SIZE = 100

    @Bean
    fun diaryReminderJob(): Job =
        JobBuilder("diaryReminderJob", jobRepository)
            .start(diaryReminderStep())
            .build()

    @Bean
    fun diaryReminderStep(): Step =
        StepBuilder("diaryReminderStep", jobRepository)
            .chunk<Int, FcmMessageDto>(CHUNK_SIZE, transactionManager)
            .reader(diaryReminderReader())
            .processor(diaryReminderProcessor())
            .writer(diaryReminderWriter())
            .build()

    @Bean
    fun diaryReminderReader(): ListItemReader<Int> {
        // Redis에 FCM 토큰이 있는 사용자 ID 목록 조회
        val usersWithFcmTokens = fcmTokenService.getAllUsersWithFcmTokens()
        return ListItemReader(usersWithFcmTokens)
    }

    @Bean
    fun diaryReminderProcessor(): ItemProcessor<Int, FcmMessageDto> =
        ItemProcessor { userId ->
            val token = fcmTokenService.getFcmToken(userId)
            if (token != null) {
                FcmMessageDto(
                    userId = userId,
                    token = token,
                    notificationType = NotificationType.DIARY_REMINDER,
                    data = mapOf("reason" to "diary_reminder_notification"),
                )
            } else {
                null // 토큰이 없는 사용자는 건너뛰기
            }
        }

    @Bean
    fun diaryReminderWriter(): ItemWriter<FcmMessageDto> =
        ItemWriter { chunk ->
            chunk.forEach { message ->
                fcmService.sendMessage(message)
            }
        }
}
