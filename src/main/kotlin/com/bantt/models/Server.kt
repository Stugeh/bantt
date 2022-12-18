package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Server(
    val name: String,
    val messages: List<Message> = listOf(),
    val subscribers: List<ObjectId> = listOf(),
    @BsonId val _id: ObjectId = ObjectId()
)
