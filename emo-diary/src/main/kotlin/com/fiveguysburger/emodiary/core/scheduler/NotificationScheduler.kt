package com.fiveguysburger.emodiary.core.scheduler

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException
import org.springframework.batch.core.repository.JobRestartException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@Configuration
class NotificationScheduler(
    private val jobLauncher: JobLauncher,
    @Qualifier("inactiveUserNotifyJob") private val inactiveUserNotifyJob: Job,
) {
    private val logger = LoggerFactory.getLogger(NotificationScheduler::class.java)

    @Scheduled(cron = "0 0 21 * * ?")
    fun runInactiveUserNotifyJob() {
        try {
            logger.info("InactiveUserNotifyJob 시작: ${LocalDateTime.now()}")

            val jobParameters =
                JobParametersBuilder()
                    .addString("runAt", LocalDateTime.now().toString())
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters()

            val jobExecution = jobLauncher.run(inactiveUserNotifyJob, jobParameters)

            logger.info("InactiveUserNotifyJob 완료: JobExecutionId=${jobExecution.id}, Status=${jobExecution.status}")
        } catch (e: JobExecutionAlreadyRunningException) {
            logger.warn("InactiveUserNotifyJob이 이미 실행 중입니다: ${e.message}")
        } catch (e: JobRestartException) {
            logger.error("InactiveUserNotifyJob 재시작 실패: ${e.message}", e)
        } catch (e: JobInstanceAlreadyCompleteException) {
            logger.warn("InactiveUserNotifyJob이 이미 완료되었습니다: ${e.message}")
        } catch (e: Exception) {
            logger.error("InactiveUserNotifyJob 실행 중 오류 발생: ${e.message}", e)
        }
    }
}
