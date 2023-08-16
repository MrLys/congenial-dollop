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
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.session.HttpSessionEventPublisher
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
    private val jwtTokenFilter: JwtTokenFilter,
    private val requestFilter: RequestFilter,
    private val longLivedTokenAuthenticationFilter: LongLivedTokenAuthenticationFilter,
    private val entryPoint: EntryPoint,
    private val accessDenied: AccessDenied,
    @Value("\${budzilla.cors.allowed_origins}")
    private val corsAllowedOrigins: String,
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
            .exceptionHandling()
            .authenticationEntryPoint(entryPoint)
            .accessDeniedHandler(accessDenied)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .mvcMatchers("/admin/**").hasAuthority(Role.ADMIN.name)
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
        http.addFilterBefore(longLivedTokenAuthenticationFilter, JwtTokenFilter::class.java)
        http.addFilterBefore(requestFilter, LongLivedTokenAuthenticationFilter::class.java)
        return http.build()
    }
    @Bean
    fun mySecurityContextRepository() : HttpSessionSecurityContextRepository {
        return HttpSessionSecurityContextRepository()
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
    @Bean
    fun httpSessionEventPublisher() : HttpSessionEventPublisher {
        return HttpSessionEventPublisher()
    }


}
