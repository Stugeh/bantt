package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class User(
    val username: String,
    val password: String,
    val subscriptions: List<UUID> = listOf(),
    @BsonId val id: UUID = UUID.randomUUID()
)
