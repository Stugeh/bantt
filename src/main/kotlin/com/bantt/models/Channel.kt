package com.bantt.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Channel(
    val name: String,
    val messages: List<Message> = listOf(),
    @BsonId val _id: ObjectId = ObjectId()
)
