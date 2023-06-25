package com.xebia.carbonaware.producer.queue

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.*
import com.xebia.carbonaware.producer.Task
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.util.*


@Service
class QueueService(private val amazonSQS: AmazonSQS, @Value("\${aws.base-url}\${aws.queue.url}") private val queueUrl: String) {
    @EventListener
    fun createQueueJustInCase(event: ApplicationReadyEvent) {
        amazonSQS.createQueue("task-queue")
    }

    fun publishTask(task: Task): SendMessageResult? {
        return publishMessage(Json.encodeToString(task))
    }

    fun publishMessage(message: String): SendMessageResult? {
        return try {
            val sendMessageRequest = SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message)
            return amazonSQS.sendMessage(sendMessageRequest)
        } catch (e: Exception) {
            LOGGER.error("Exception e : {}", e.message)
            null
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(QueueService::class.java)
    }
}