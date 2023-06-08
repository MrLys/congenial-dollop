package com.budzilla.auth

import com.budzilla.common.Metrics
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

@Component
class AccessDenied(
    private val metrics: Metrics
) : AccessDeniedHandler {
    private val mapper = jacksonObjectMapper()
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: org.springframework.security.access.AccessDeniedException?
    ) {
        metrics.accessDeniedCounter.increment()
        val errorObject = HashMap<String, Any>()
        val errorCode = 403
        errorObject["message"] = "Forbidden access of protected resource, insufficient privileges"
        errorObject["error"] = HttpStatus.FORBIDDEN
        errorObject["code"] = errorCode
        errorObject["timestamp"] = Timestamp(Date().time)
        response?.contentType = "application/json"
        response?.characterEncoding = "UTF-8"
        response?.status = errorCode
        response?.writer?.write(mapper.writeValueAsString(errorObject))
    }
}