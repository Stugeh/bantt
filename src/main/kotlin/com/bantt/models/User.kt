package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val username: String,
    val password: String,
    val subscriptions: List<ObjectId> = listOf(),
    @BsonId val _id: ObjectId = ObjectId()
)
