package com.bantt.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.bantt.models.LoginRequest
import com.bantt.models.User
import com.bantt.models.Users
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


val secret: String = dotenv()["JWT_SECRET"]
val issuer: String = dotenv()["JWT_ISSUER"]
val audience: String = dotenv()["JWT_AUDIENCE"]
val myRealm: String = dotenv()["JWT_REALM"]

fun Application.configureSecurity() {
    authentication {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    routing {
        post("/signup") {
            val request = kotlin.runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
            // TODO change min length
            val isPwTooShort = request.password.length < 3
            if (areFieldsBlank || isPwTooShort) {
                call.respond(HttpStatusCode.Conflict)
                return@post
            }

            val passwordHash = BCrypt.hashpw(request.password, BCrypt.gensalt())
            transaction {
                val user = Users.insertAndGetId {
                    it[username] = username
                    it[password] = passwordHash
                }
            }


            call.respond(HttpStatusCode.OK)
        }
        post("/login") {
            val request = kotlin.runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val user = User.find { Users.username eq request.username }.firstOrNull()

            if (user == null) {
                call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                return@post
            }

            val isValidPassword = BCrypt.checkpw(request.password, user.password)

            if (!isValidPassword) {
                call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                return@post
            }

            val monthFromNow = Date.from(
                LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant()
            )

            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", user.username)
                .withExpiresAt(monthFromNow)
                .sign(Algorithm.HMAC256(secret))

            call.respond(hashMapOf("token" to "Bearer $token"))
        }

        authenticate("auth-jwt") {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }

    }
}
