package com.xebia.carbonaware.consumer.task

import kotlinx.serialization.Serializable

@Serializable
data class Task(val id: String, val taskNumber: Int)