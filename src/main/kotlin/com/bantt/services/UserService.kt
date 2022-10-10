package com.bantt.services

import com.bantt.models.User
import com.bantt.models.Users
import kotlinx.coroutines.runBlocking

fun User.Companion.findByUsername(username: String): User? {
    return runBlocking {
        User.find { Users.username eq username }.firstOrNull()
    }
}
