package com.fiveguysburger.emodiary.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class EmoDiaryApplication

fun main(args: Array<String>) {
    runApplication<EmoDiaryApplication>(*args)
}
