package com.fiveguysburger.emodiary.core.config

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@EnableBatchProcessing
class BatchConfig(
    private val dataSource: DataSource
) : DefaultBatchConfiguration() 