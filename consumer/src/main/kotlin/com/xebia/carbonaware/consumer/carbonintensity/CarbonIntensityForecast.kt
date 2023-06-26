package com.xebia.carbonaware.consumer.carbonintensity

import kotlinx.serialization.Serializable


@Serializable
data class OptimalDataPoint(val value: Double, val location: String, val timestamp: String)

@Serializable
data class CarbonIntensityForecast(
    val optimalDataPoints: List<OptimalDataPoint>, val location: String
)
