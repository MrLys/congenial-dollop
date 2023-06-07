package com.budzilla.auth

import com.budzilla.common.Metrics
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class EntryPoint(private val metrics: Metrics) : AuthenticationEntryPoint {
    private val mapper = jacksonObjectMapper()
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        metrics.unauthorizedRequestCounter.increment()
        val errorObject = HashMap<String, Any>()
        val errorCode = 401
        errorObject["message"] = "Unauthorized access of protected resource, invalid credentials"
        errorObject["error"] = HttpStatus.UNAUTHORIZED
        errorObject["code"] = errorCode
        errorObject["timestamp"] = Timestamp(Date().time)
        response?.contentType = "application/json"
        response?.characterEncoding = "UTF-8"
        response?.status = errorCode
        response?.writer?.write(mapper.writeValueAsString(errorObject))
    }

}