package com.bantt.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    install(Locations) {
    }

    routing {
        authenticate {
            route("/users") {
                get {
                }

                get("{id?}") {
                }
            }

            route("/servers") {
                get {
                }

                get("{id?}") {
                }

                post {
                }
            }
        }
    }
}

