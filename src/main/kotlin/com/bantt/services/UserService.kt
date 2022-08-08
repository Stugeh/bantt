package com.bantt.services

import com.bantt.models.User
import com.bantt.repositories.UserRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.litote.kmongo.eq

class UserService : KoinComponent {
    companion object {
        private val repository = UserRepository().collection
        fun insertUser(user: User): Boolean {
            return false
        }

        fun getUserByUsername(username: String): User? {
            runBlocking {
                repository.findOne(User::username eq username)
            }
            return null
        }
    }
}
