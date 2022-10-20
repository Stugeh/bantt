package com.bantt.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*


class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(Users)

    val username by Users.username
    val password by Users.password
    val createdAt by Users.createdAt
    val updatedAt by Users.updatedAt
    fun toResult() = UserResult(id.toString(), username, createdAt.toString(), updatedAt.toString())
}

object Users : UUIDTable() {
    val username = varchar("username", 127)
    val password = varchar("password", 127)
    val createdAt = datetime("createdAt")
    val updatedAt = datetime("updatedAt")
}

data class UserResult(
    val id: String,
    val username: String,
    val createdAt: String,
    val updatedAt: String
)

fun rowToUser(row: ResultRow): UserResult {
    return UserResult(
        row[Users.id].toString(),
        row[Users.username],
        row[Users.createdAt].toString(),
        row[Users.updatedAt].toString()
    )
}
