package com.bantt.dao

import com.bantt.models.User
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.litote.kmongo.coroutine.CoroutineDatabase

class UserRepository : BaseDao<User>(), KoinComponent {
    private val db: CoroutineDatabase by inject()

    override val collection get() = db.getCollection<User>()

    init {
        runBlocking {
            collection.ensureUniqueIndex(User::username)
        }
    }

}
