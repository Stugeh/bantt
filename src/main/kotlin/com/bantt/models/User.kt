package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.*

data class User(
    val username: String,
    val password: String,
    val subscriptions: List<UUID> = listOf(),
    @BsonId val _id: ObjectId = ObjectId()
)
