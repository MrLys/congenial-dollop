package com.budzilla.common

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestFilter(private val metrics: Metrics) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestId = UUID.randomUUID().toString()
        try {
            //lambda
            metrics.requestLatency.record { filterChain.doFilter(request, response) }
        } catch (exception: Exception) {
            println(exception.message)
            throw exception
        } finally {
            metrics.requestCounter.increment()
            response.addHeader("X-Request-Id", requestId)
        }
    }
}