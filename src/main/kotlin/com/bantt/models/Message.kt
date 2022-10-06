package com.bantt.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

class Message(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Message>(Messages)

    val body by Messages.body
    val createdAt by Messages.createdAt
    val updatedAt by Messages.updatedAt

    val user by User referencedOn Messages.user
    val channel by Channel referencedOn Messages.channel
}

object Messages : UUIDTable() {
    val body = varchar("body", 1080)
    val createdAt = datetime("createdAt")
    val updatedAt = datetime("updatedAt")

    val user = reference("user", Users)
    val channel = reference("channel", Channels)

}

