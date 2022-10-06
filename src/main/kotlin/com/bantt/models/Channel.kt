package com.bantt.models

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.time.LocalDate
import java.util.*

interface Channel : Entity<Channel> {
    val id: UUID
    val name: String
    val server: Server
    val description: String
    val createdAt: LocalDate
    val updatedAt: LocalDate
}

object Channels : Table<Channel>("t_channel") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val server = uuid("serverId").references(Servers) { it.server }
    val description = varchar("description").bindTo { it.description }
    val createdAt = date("createdAt").bindTo { it.createdAt }
    val updatedAt = date("updatedAt").bindTo { it.updatedAt }
}
