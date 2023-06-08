package com.budzilla.common
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.prometheus.client.Summary
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class Metrics (meterRegistry: MeterRegistry){
    val requestLatency: Timer = Timer.builder("request.latency")
        .description("A summary of the time it takes for a request to be processed")
        .register(meterRegistry)
    val requestCounter: Counter = Counter.builder("request.counter")
        .description("A counter to track the number of requests")
        .register(meterRegistry)
    val unauthorizedRequestCounter: Counter = Counter.builder("unauthorized.counter")
        .description("A counter to track the number of unauthorized requests")
        .register(meterRegistry)
    val accessDeniedCounter: Counter = Counter.builder("accessDenied.counter")
        .description("A counter to track the number of access denied requests")
        .register(meterRegistry)
    val successfulRequestCounter: Counter = Counter.builder("successful.counter")
        .description("A counter to track the number of successfull requests")
        .register(meterRegistry)

    val loginAttempts: Counter = Counter.builder("loginAttempts.counter")
        .description("A counter to track the number of login attempts")
        .register(meterRegistry)
    val failedLoginAttempts: Counter = Counter.builder("failedLoginAttempts.counter")
        .description("A counter to track the number of failed login attempts")
        .register(meterRegistry)

    val newEntryCreated: Counter = Counter.builder("newEntryCreated.counter")
        .description("A counter to track the number of new entries created")
        .register(meterRegistry)

    val newUsersCreated: Counter = Counter.builder("newUsersCreated.counter")
        .description("A counter to track the number of new users created")
        .register(meterRegistry)
    val entriesFetched: Counter = Counter.builder("entriesFetched.counter")
        .description("A counter to track the number of new entries created")
        .register(meterRegistry)

}