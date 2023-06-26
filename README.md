# Carbon Aware Consumer Demo using CarbonAwareSDK
CarbonAwareSDK helps us retrieve information about the carbon intensity (gCO2/kwh) in a certain time and place.
This repo contains an example project where we use this API to build two carbon aware consumers.

## Project setup

Our setup contains three components:

1. `localstack`, for providing a local AWS SQS queue.
2. `producer`, which puts a random task description on the queue every 5 seconds.
3. `consumer`, implementing three consumers:
    1. Threshold-based consumer, which only consumes new tasks from the queue if the carbon intensity is lower than a
       certain threshold.
    2. Adaptive velocity consumer, which takes longer breaks between tasks based on the carbon intensity.
   3. Forecast-based consumer, which runs its daily task at the optimal time in the next 24 hours.

## Instructions

```bash
docker compose up --build
```