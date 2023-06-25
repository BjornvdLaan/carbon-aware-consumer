package com.xebia.carbonaware.consumer.queue

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AWSConfig {
    fun credentials(): AWSCredentials {
        return BasicAWSCredentials(
            "accesskey",
            "secretkey"
        )
    }

    @Bean
    fun amazonSQS(@Value("\${aws.base-url}") baseUrl: String): AmazonSQS {
        return AmazonSQSClientBuilder
            .standard()
            .withEndpointConfiguration(getEndpointConfiguration(baseUrl))
            .withCredentials(AWSStaticCredentialsProvider(credentials()))
            .build()
    }

    private fun getEndpointConfiguration(url: String): AwsClientBuilder.EndpointConfiguration {
        return AwsClientBuilder.EndpointConfiguration(url, Regions.EU_WEST_1.getName())

    }
}