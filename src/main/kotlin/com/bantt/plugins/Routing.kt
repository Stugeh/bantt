package com.bantt.plugins

import com.bantt.authenticate
import com.bantt.getSecretInfo
import com.bantt.security.hashing.HashingService
import com.bantt.security.token.TokenConfig
import com.bantt.security.token.TokenService
import com.bantt.signIn
import com.bantt.signUp
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    install(Locations) {
    }

    fun Application.configureRouting(
        hashingService: HashingService,
        tokenService: TokenService,
        tokenConfig: TokenConfig
    ) {
        routing {
            signIn(hashingService, tokenService, tokenConfig)
            signUp(hashingService)
            authenticate()
            getSecretInfo()
        }
    }
}
