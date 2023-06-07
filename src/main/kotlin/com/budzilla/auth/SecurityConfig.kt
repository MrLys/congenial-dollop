package com.budzilla.auth

import com.budzilla.common.RequestFilter
import com.budzilla.model.Role
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
class SecurityConfig(
    val jwtTokenFilter: JwtTokenFilter,
    val requestFilter: RequestFilter,
    val entryPoint: EntryPoint,
    @Value("\${budzilla.cors.allowed_origins}")
    val corsAllowedOrigins: String,
)  {
    @Bean
    fun passwordEncoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }
    @Bean
    fun authManager(http : HttpSecurity, passwordEncoder : PasswordEncoder, userService : UserDetailsServiceImpl) : AuthenticationManager {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build()
    }

    @Bean
    fun filterChain(http : HttpSecurity) : SecurityFilterChain {
        http.cors()
            .and()
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(entryPoint)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .mvcMatchers("/actuator/**").hasAuthority(Role.METRICS.name)
            .and()
            .authorizeRequests().antMatchers("/api/auth/**").permitAll()
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(requestFilter, JwtTokenFilter::class.java)
        return http.build()
    }
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        val originsAsList = corsAllowedOrigins.split(",")
        configuration.allowedOrigins = originsAsList
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "OPTIONS", "DELETE")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}
