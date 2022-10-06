package com.bantt.models

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.time.LocalDate
import java.util.*

interface User : Entity<User> {
    val id: UUID
    val username: String
    val password: String
    val createdAt: LocalDate
}

object Users : Table<User>("t_user") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val username = varchar("username").bindTo { it.username }
    val password = varchar("password").bindTo { it.password }
    val createdAt = date("createdAt").bindTo { it.createdAt }
}
