package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

data class User(
    val username: String,
    val password: String,
    val settings: UserSettings = UserSettings("setting"),
    val updatedAt: Instant = Instant.now(),
    val createdAt: Instant = Instant.now(),
    val subscriptions: List<ObjectId> = listOf(),
    @BsonId val _id: ObjectId = ObjectId()
)

data class UserSettings(
    val setting: String
)
