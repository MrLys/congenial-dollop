package com.budzilla.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
class SecurityConfig(
    val jwtTokenFilter: JwtTokenFilter
) {
    class EntryPoint : AuthenticationEntryPoint {
        private val mapper = jacksonObjectMapper()
        override fun commence(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authException: AuthenticationException?
        ) {
            val errorObject = HashMap<String, Any>()
            val errorCode = 401
            errorObject["message"] = "Unauthorized access of protected resource, invalid credentials"
            errorObject["error"] = HttpStatus.UNAUTHORIZED
            errorObject["code"] = errorCode
            errorObject["timestamp"] = Timestamp (Date().time)
            response?.contentType = "application/jsoncharset=UTF-8"
            response?.status = errorCode
            response?.writer?.write(mapper.writeValueAsString(errorObject))
        }

    }
    @Bean
    fun passwordEncoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }
    @Bean
    fun authManager(http : HttpSecurity, passwordEncoder : PasswordEncoder, userService : UserDetailsService) : AuthenticationManager {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build()
    }

    @Bean
    fun filterChain(http : HttpSecurity) : SecurityFilterChain {
        http.csrf().disable()
            .exceptionHandling().authenticationEntryPoint(EntryPoint())
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests().antMatchers("/api/auth/**").permitAll()
            .anyRequest()
            .authenticated()

            http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
