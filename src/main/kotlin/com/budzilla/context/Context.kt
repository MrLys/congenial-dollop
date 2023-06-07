package com.budzilla.context

import com.budzilla.auth.UserPrincipal
import com.budzilla.model.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class Context {
    fun getUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        return (auth.principal as UserPrincipal).user
    }
    fun getUserId(): Long {
        return getUser().id!!
    }
}