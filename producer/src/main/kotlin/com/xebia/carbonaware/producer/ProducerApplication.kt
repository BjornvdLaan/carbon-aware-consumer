package com.xebia.carbonaware.producer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class ProducerApplication

fun main(args: Array<String>) {
    runApplication<ProducerApplication>(*args)
}
