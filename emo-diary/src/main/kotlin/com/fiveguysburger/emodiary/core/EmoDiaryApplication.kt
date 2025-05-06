package com.fiveguysburger.emodiary.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmoDiaryApplication

fun main(args: Array<String>) {
	runApplication<EmoDiaryApplication>(*args)
}
