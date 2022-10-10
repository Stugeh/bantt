package com.bantt.plugins

import com.bantt.dbQuery
import com.bantt.models.*
import com.bantt.services.findByUsername
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDateTime
import java.util.*


fun Application.configureRouting() {
    install(Locations) {
    }

    routing {
        authenticate {
            route("/users") {
                get {
                    val users = dbQuery { Users.selectAll() }
                    call.respond(HttpStatusCode.OK, users)
                }
            }
            route("/servers") {
                get {
                    val servers = dbQuery { Servers.selectAll() }
                    call.respond(HttpStatusCode.OK, servers)
                }

                get("{id?}") {
                    val parsedId = UUID.fromString(call.parameters["id"]) ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Missing id"
                    )
                    val server = dbQuery { Server[parsedId] }
                    call.respond(HttpStatusCode.OK, server)
                }

                post {
                    val server = call.receive<NewServer>()
                    val ownerUsername = call
                        .principal<JWTPrincipal>()!!
                        .payload
                        .getClaim("username")
                        .asString()

                    val owner = dbQuery { User.findByUsername(ownerUsername) } ?: return@post call.respond(
                        HttpStatusCode.BadRequest, "Owner not found"
                    )

                    dbQuery {
                        Servers.insert {
                            it[name] = server.name
                            it[private] = server.private
                            it[createdAt] = LocalDateTime.now()
                            it[updatedAt] = LocalDateTime.now()
                            it[this.owner] = owner.id
                        }
                    }
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}

