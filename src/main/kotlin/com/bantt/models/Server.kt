package com.bantt.models


import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

class Server(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Server>(Servers)

    val name by Servers.name
    val private by Servers.private
    val createdAt by Servers.createdAt
    val updatedAt by Servers.updatedAt

    val owner by User referencedOn Servers.owner
}

object Servers : UUIDTable() {
    val name = varchar("name", 128)
    val private = bool("private")
    val createdAt = datetime("createdAt")
    val updatedAt = datetime("updatedAt")
    
    val owner = reference("owner", Users)
}
