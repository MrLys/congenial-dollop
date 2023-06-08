package com.budzilla.auth

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class LongLivedTokenAuthentication(
    private val token: String,
    authorities: Collection<GrantedAuthority>
) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): Any = token
    override fun getPrincipal(): Any = token
}