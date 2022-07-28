package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class Channel(
    @BsonId
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val messages: List<Message>
)
