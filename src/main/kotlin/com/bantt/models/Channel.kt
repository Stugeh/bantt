package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class Channel(
    val name: String,
    val messages: List<Message> = listOf(),
    @BsonId val id: UUID = UUID.randomUUID()
)
