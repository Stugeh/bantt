package com.bantt.plugins

import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class RoutingKtTest {

    @Test
    fun testGetTest() = testApplication {
        application {
            configureRouting()
        }
        client.get("/test").apply {
            assert(this != null)
        }
    }
}
