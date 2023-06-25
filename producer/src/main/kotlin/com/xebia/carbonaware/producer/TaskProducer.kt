package com.xebia.carbonaware.producer

import com.xebia.carbonaware.producer.queue.QueueService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TaskProducer(val queueService: QueueService) {

    @Scheduled(fixedDelay = 5000)
    fun produceTask() {
        val result = queueService.publishTask(Task.randomTask())
        return if (result != null) {
            LOGGER.info("Task is successfully published in message ${result.messageId}.")
        } else {
            LOGGER.error("Task publishing failed.")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(QueueService::class.java)
    }
}