package com.bantt.models

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.time.LocalDate
import java.util.*

interface Message : Entity<Message> {
    val id: UUID
    val user: User
    val channel: Channel
    val body: String
    val createdAt: LocalDate
    val updatedAt: LocalDate
}

object Messages : Table<Message>("t_message") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val user = uuid("userId").references(Users) { it.user }
    val channel = uuid("channelId").references(Channels) { it.channel }
    val body = varchar("body").bindTo { it.body }
    val createdAt = date("createdAt").bindTo { it.createdAt }
    val updatedAt = date("updatedAt").bindTo { it.updatedAt }
}

