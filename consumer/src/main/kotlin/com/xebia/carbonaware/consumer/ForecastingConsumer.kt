package com.xebia.carbonaware.consumer

import com.xebia.carbonaware.consumer.carbonintensity.CarbonIntensityService
import com.xebia.carbonaware.consumer.queue.QueueService
import com.xebia.carbonaware.consumer.task.TaskProcessor
import org.slf4j.LoggerFactory
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

/**
 * Strategy 3: Once per day, this strategy retrieves the moment in the next 24 hours with the lowest carbon intensity and schedules the task then.
 */
@Component
class ForecastingConsumer(
    val taskScheduler: TaskScheduler,
    val taskProcessor: TaskProcessor,
    val carbonIntensityService: CarbonIntensityService,
    val queueService: QueueService
) {
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    fun consume() {
        val task = queueService.receiveTask()

        task?.let {
            val optimalIntensity = carbonIntensityService.getForecastedOptimalIntensity().optimalDataPoints.first()

            taskScheduler.schedule(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(optimalIntensity.timestamp))) {
                val result = taskProcessor.process(it)
                LOGGER.info("Task ${it.id} executed successfully with forecasting strategy. Result = ${result}.")
            }
            LOGGER.info("Task ${it.id} scheduled successfully at ${optimalIntensity.timestamp} with forecasting strategy.")
        }
    }


    companion object {
        private val LOGGER = LoggerFactory.getLogger(ForecastingConsumer::class.java)
    }

    fun TaskScheduler.schedule(moment: Instant, task: Runnable) =
        this.schedule(task, moment)
}