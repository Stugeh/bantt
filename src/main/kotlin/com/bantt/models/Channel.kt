package com.bantt.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

class Channel(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Channel>(Channels)

    var name by Channels.name
    var description by Channels.description
    var createdAt by Channels.createdAt
    var updatedAt by Channels.updatedAt

    var server by User referencedOn Channels.server
}

object Channels : UUIDTable() {
    val name = varchar("name", 127)
    val description = varchar("description", 1080)
    val createdAt = datetime("createdAt")
    val updatedAt = datetime("modifiedAt")
    val server = reference("server", Servers)
}
