package com.xebia.carbonaware.consumer

import com.xebia.carbonaware.consumer.carbonintensity.CarbonIntensityService
import com.xebia.carbonaware.consumer.queue.QueueService
import com.xebia.carbonaware.consumer.task.TaskProcessor
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

/**
 * Strategy 1: Only consume messages when carbon intensity is below a certain threshold.
 */
//@Component
class ThresholdConsumer(
    val taskProcessor: TaskProcessor,
    val carbonIntensityService: CarbonIntensityService,
    val queueService: QueueService
) {
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    fun consume() {
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

    companion object {
        private const val MAXIMUM_CARBON_INTENSITY = 400
        private val LOGGER = LoggerFactory.getLogger(ThresholdConsumer::class.java)
    }
}