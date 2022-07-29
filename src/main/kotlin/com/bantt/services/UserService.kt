package com.bantt.services

import com.bantt.dao.UserRepository
import com.bantt.models.User
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.litote.kmongo.json

class UserService : KoinComponent {
    private val repository = UserRepository()

    fun test(): Boolean {
        val newUser = User("firstName", "password")
        return runBlocking { repository.save(newUser.json) }
    }
}
