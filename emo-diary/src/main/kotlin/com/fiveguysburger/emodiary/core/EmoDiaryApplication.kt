package com.fiveguysburger.emodiary.core

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableRabbit
@ComponentScan(basePackages = ["com.fiveguysburger.emodiary"])
class EmoDiaryApplication

fun main(args: Array<String>) {
    runApplication<EmoDiaryApplication>(*args)
}
