package com.bantt.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

class Subscription(id: EntityID<UUID>) : UUIDEntity(id) {
    val user by User referencedOn Subscriptions.user
    val channel by Channel referencedOn Subscriptions.channel
    val createdAt by Subscriptions.createdAt
}

object Subscriptions : UUIDTable() {
    val user = reference("user", Users)
    val channel = reference("channel", Channels)
    val createdAt = datetime("createdAt")
}
