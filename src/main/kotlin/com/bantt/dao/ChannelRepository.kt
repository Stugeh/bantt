package com.bantt.dao

import com.bantt.models.Channel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.litote.kmongo.coroutine.CoroutineDatabase

class ChannelRepository : BaseDao<Channel>(), KoinComponent {
    private val db: CoroutineDatabase by inject()

    override val collection get() = db.getCollection<Channel>()
}
