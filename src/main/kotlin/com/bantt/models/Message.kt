package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import java.time.Instant
import java.util.*

data class Message(
    val user: User,
    val body: String,
    val date: Instant = Instant.now(),
    @BsonId val id: UUID = UUID.randomUUID()
)

