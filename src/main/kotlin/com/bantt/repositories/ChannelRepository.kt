package com.bantt.repositories

import com.bantt.models.Channel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.litote.kmongo.coroutine.CoroutineDatabase

class ChannelRepository : BaseRepository<Channel>(), KoinComponent {
    private val db: CoroutineDatabase by inject()

    override val collection = db.getCollection<Channel>()
}
