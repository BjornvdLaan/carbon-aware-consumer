package com.xebia.carbonaware.consumer.carbonintensity

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CarbonIntensityService {
    private val httpClient: HttpClient
        get() = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest { url("https://carbon-aware-api.azurewebsites.net") }
        }

    // Note: please do not use runBlocking in actual production scenarios
    fun getIntensity(): CarbonIntensity = runBlocking {
        val data = httpClient
            .get("/emissions/bylocation") {
                url {
                    parameters.append("location", LOCATION)
                }
            }
            .body<List<CarbonIntensity>>()
            .first()

        LOGGER.info("Retrieved intensity ${data.rating} from CarbonAwareSDK for ${data.location} at ${data.time}.")
        return@runBlocking data
    }

    fun getForecastedOptimalIntensity(): CarbonIntensityForecast = runBlocking {
        val data = httpClient
            .get("/emissions/forecasts/current") {
                url {
                    parameters.append("location", LOCATION)
                }
            }
            .body<List<CarbonIntensityForecast>>()
            .first()

        val optimalDataPoint = data.optimalDataPoints.first()
        LOGGER.info("Retrieved intensity forecast from CarbonAwareSDK with optimal point ${optimalDataPoint.value}  for ${optimalDataPoint.location} at ${optimalDataPoint.timestamp}.")
        return@runBlocking data
    }

    companion object {
        private const val LOCATION = "eastus"
        private val LOGGER = LoggerFactory.getLogger(CarbonIntensityService::class.java)
    }
}