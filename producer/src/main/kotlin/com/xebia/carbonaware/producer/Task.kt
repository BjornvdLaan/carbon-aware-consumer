package com.xebia.carbonaware.producer

import kotlinx.serialization.Serializable
import java.util.*
import kotlin.random.Random

@Serializable
data class Task(val id: String, val taskNumber: Int) {
    companion object {
        fun randomTask() = Task(UUID.randomUUID().toString(), Random.nextInt())
    }
}
