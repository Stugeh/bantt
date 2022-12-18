package com.bantt.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

import com.bantt.models.LoginRequest
import com.bantt.models.User
import com.bantt.services._userCollection

import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.eq
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


val secret: String = dotenv()["JWT_SECRET"]
val issuer: String = dotenv()["JWT_ISSUER"]
val audience: String = dotenv()["JWT_AUDIENCE"]
val myRealm: String = dotenv()["JWT_REALM"]


//fun Route.jwtAuth(block: Route) {
//    println("sadasd")
//    authenticate("auth-jwt") { block() }
//}

fun Application.configureSecurity() {
    authentication {
        jwt {
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
            val response: HttpStatusCode = runBlocking {
                val request = call.receiveNullable<LoginRequest>()
                    ?: return@runBlocking HttpStatusCode.BadRequest

                // TODO change min length
                val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
                val pwTooShort = request.password.length < 3
                if (areFieldsBlank || pwTooShort) return@runBlocking HttpStatusCode.BadRequest

                val passwordHash = BCrypt.hashpw(request.password, BCrypt.gensalt())

                _userCollection.insertOne(User(request.username, passwordHash))
                val newUser = _userCollection.findOne(User::username eq request.username)

                if (newUser != null) {
                    return@runBlocking HttpStatusCode.Created
                }

                return@runBlocking HttpStatusCode.BadRequest
            }
            call.respond(response)
        }

        post("/login") {
            val token: String = runBlocking {
                val request = call.receiveNullable<LoginRequest>()
                    ?: return@runBlocking ""

                val user = _userCollection.findOne(User::username eq request.username)
                    ?: return@runBlocking ""

                val isValidPassword = BCrypt.checkpw(request.password, user.password)

                if (!isValidPassword) {
                    return@runBlocking ""
                }

                // month from now
                val expiryDate = Date.from(
                    LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant()
                )

                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", user.username)
                    .withExpiresAt(expiryDate)
                    .sign(Algorithm.HMAC256(secret))

                return@runBlocking "Bearer $token"
            }

            val status = if (token.isNotEmpty()) HttpStatusCode.OK else HttpStatusCode.Unauthorized

            call.respond(status, token)
        }

        authenticate {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }

    }
}
