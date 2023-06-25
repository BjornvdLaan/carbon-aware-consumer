package com.xebia.carbonaware.consumer.queue

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.*
import com.xebia.carbonaware.consumer.task.Task
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class QueueService(private val amazonSQS: AmazonSQS, @Value("\${aws.base-url}\${aws.queue.url}") private val queueUrl: String) {
    fun receiveTask(): Task? {
        val message = receiveMessage()
        return if (message != null) {
            LOGGER.info("Task received with id ${message.messageId}.")
            Json.decodeFromString<Task>(message.body)
        } else {
            LOGGER.error("No tasks waiting in the queue.")
            null
        }
    }

    fun receiveMessage(): Message? {
        val receiveMessageRequest = ReceiveMessageRequest()
        receiveMessageRequest.queueUrl = queueUrl
        receiveMessageRequest.waitTimeSeconds = 5
        receiveMessageRequest.maxNumberOfMessages = 1
        return amazonSQS.receiveMessage(receiveMessageRequest).messages.firstOrNull()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(QueueService::class.java)
    }
}