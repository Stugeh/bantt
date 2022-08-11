package com.bantt.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.bantt.models.User
import com.bantt.models.requests.AuthRequest
import com.bantt.services._userCollection
import com.bantt.services.getByUsername
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
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
            val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
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
            val user = User(
                username = request.username,
                password = passwordHash
            )
            val wasAcknowledged = _userCollection.save(user)?.wasAcknowledged()
            if (!wasAcknowledged!!) {
                call.respond(HttpStatusCode.Conflict)
                return@post
            }

            call.respond(HttpStatusCode.OK)
        }
        post("/login") {
            val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val user = _userCollection.getByUsername(request.username)

            if (user == null) {
                call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                return@post
            }

            val isValidPassword = BCrypt.checkpw(request.password, user.password)

            if (!isValidPassword) {
                call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                return@post
            }

            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", user.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(secret))
            call.respond(hashMapOf("token" to token))
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
