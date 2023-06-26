package com.xebia.carbonaware.consumer

import com.xebia.carbonaware.consumer.carbonintensity.CarbonIntensityService
import com.xebia.carbonaware.consumer.queue.QueueService
import com.xebia.carbonaware.consumer.task.TaskProcessor
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener

/**
 * Strategy 2: Adapt the time between tasks based on the carbon intensity.
 */
//@Component
class AdaptiveVelocityConsumer(
    val taskProcessor: TaskProcessor,
    val carbonIntensityService: CarbonIntensityService,
    val queueService: QueueService
) {
    @EventListener
    fun consume(event: ApplicationReadyEvent) {
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
        private val LOGGER = LoggerFactory.getLogger(AdaptiveVelocityConsumer::class.java)
    }
}