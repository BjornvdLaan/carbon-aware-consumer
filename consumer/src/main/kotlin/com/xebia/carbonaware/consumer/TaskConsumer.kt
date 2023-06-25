package com.xebia.carbonaware.consumer

import com.xebia.carbonaware.consumer.carbonintensity.CarbonIntensityService
import com.xebia.carbonaware.consumer.queue.QueueService
import com.xebia.carbonaware.consumer.task.TaskProcessor
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class TaskConsumer(
    val taskProcessor: TaskProcessor,
    val carbonIntensityService: CarbonIntensityService,
    val queueService: QueueService
) {

    /**
     * Strategy 1: Only consume messages when carbon intensity is below a certain threshold.
     */
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    fun consumeTaskWithCarbonThreshold() {
        val currentCarbonIntensity = carbonIntensityService.getIntensity().rating

        if (currentCarbonIntensity < MAXIMUM_CARBON_INTENSITY) {
            val task = queueService.receiveTask()
            task?.let {
                val result = taskProcessor.process(it)
                LOGGER.info("Task ${it.id} executed successfully with threshold strategy. Result = ${result}.")
            }
        } else {
            LOGGER.info("No task executed with threshold strategy because carbon intensity ${currentCarbonIntensity} is too high (threshold = ${MAXIMUM_CARBON_INTENSITY}).")
        }
    }

    /**
     * Strategy 2: Adapt the time between tasks based on the carbon intensity.
     */
    @EventListener
    fun consumeTaskWithAdaptiveSpeed(event: ApplicationReadyEvent) {
        while (true) {
            val currentCarbonIntensity = carbonIntensityService.getIntensity()

            val delay = currentCarbonIntensity.rating * 10L
            Thread.sleep(delay.toLong())

            val task = queueService.receiveTask()
            task?.let {
                val result = taskProcessor.process(it)
                LOGGER.info("Task ${it.id} executed successfully with adapted speed strategy. Result = ${result}.")
            }
        }
    }

    companion object {
        private const val MAXIMUM_CARBON_INTENSITY = 400
        private val LOGGER = LoggerFactory.getLogger(TaskConsumer::class.java)
    }
}