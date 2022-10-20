package com.bantt.models


import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

class Server(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Server>(Servers)

    val name by Servers.name
    val private by Servers.private
    val createdAt by Servers.createdAt
    val updatedAt by Servers.updatedAt

    val owner by User referencedOn Servers.owner
    fun toResult() =
        ServerResult(id.toString(), name, private, createdAt.toString(), updatedAt.toString(), owner.id.toString())
}

object Servers : UUIDTable() {
    val name = varchar("name", 128)
    val private = bool("private")
    val createdAt = datetime("createdAt")
    val updatedAt = datetime("updatedAt")

    val owner = reference("owner", Users)
}

data class ServerResult(
    val id: String,
    val name: String,
    val private: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val owner: String
)

data class NewServer(
    val name: String,
    val private: Boolean
)

fun rowToServer(row: ResultRow): ServerResult {
    return ServerResult(
        row[Servers.id].toString(),
        row[Servers.name],
        row[Servers.private],
        row[Servers.createdAt].toString(),
        row[Servers.updatedAt].toString(),
        row[Servers.owner].toString()
    )
}
