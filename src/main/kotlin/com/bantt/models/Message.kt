package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

data class Message(
    val user: User,
    val body: String,
    val updatedAt: Instant = Instant.now(),
    val createdAt: Instant = Instant.now(),
    @BsonId val _id: ObjectId = ObjectId()
)
