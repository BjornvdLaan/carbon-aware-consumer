package com.xebia.carbonaware.consumer.task

import org.springframework.stereotype.Component

@Component
class TaskProcessor {
    fun process(task: Task): Int =
        task.taskNumber * task.taskNumber
}