package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import java.time.Instant
import java.util.*

data class Message(
    @BsonId
    val id: UUID = UUID.randomUUID(),
    val user: User,
    val body: String,
    val date: Instant = Instant.now()
)

