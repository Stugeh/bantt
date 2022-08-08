package com.bantt.repositories

import com.bantt.models.User
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.litote.kmongo.coroutine.CoroutineDatabase

class UserRepository : BaseRepository<User>(), KoinComponent {
    private val db: CoroutineDatabase by inject()

    override val collection = db.getCollection<User>()

    init {
        runBlocking {
            collection.ensureUniqueIndex(User::username)
        }
    }
}
