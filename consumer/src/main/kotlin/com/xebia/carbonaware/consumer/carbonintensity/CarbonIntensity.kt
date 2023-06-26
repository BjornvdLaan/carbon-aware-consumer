package com.xebia.carbonaware.consumer.carbonintensity

import kotlinx.serialization.Serializable

@Serializable
data class CarbonIntensity(val rating: Double, val location: String, val time: String, val duration: String)