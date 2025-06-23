package com.fiveguysburger.emodiary.core.scheduler

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@Configuration
class NotificationScheduler(
    private val jobLauncher: JobLauncher,
    private val inactiveUserNotifyJob: Job,
) {
    @Scheduled(cron = "0 0 21 * * ?")
    fun runInactiveUserNotifyJob() {
        val jobParameters = JobParametersBuilder()
            .addString("runAt", LocalDateTime.now().toString())
            .toJobParameters()
        jobLauncher.run(inactiveUserNotifyJob, jobParameters)
    }
} 