package com.bantt.services

import com.bantt.dao.ChannelRepository
import com.bantt.models.Channel
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.litote.kmongo.json

class ChannelService : KoinComponent {
    private val repository = ChannelRepository()

    fun test() {
        val newChannel = Channel("firstName")
        return runBlocking { repository.save(newChannel.json) }
    }
}
