package com.bantt.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(Users)

    val username by Users.username
    val password by Users.password
    val createdAt by Users.createdAt
    val updatedAt by Users.updatedAt
}

object Users : UUIDTable() {
    val username = varchar("username", 127)
    val password = varchar("password", 127)
    val createdAt = datetime("createdAt")
    val updatedAt = datetime("updatedAt")
}
