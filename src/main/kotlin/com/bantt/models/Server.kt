package com.bantt.models

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.date
import org.ktorm.schema.uuid
import java.time.LocalDate
import java.util.*

interface Server : Entity<Server> {
    val id: UUID
    val name: String
    val owner: User
    val createdAt: LocalDate
    val updatedAt: LocalDate
    val private: Boolean
}


object Servers : Table<Server>("t_server") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val owner = uuid("ownerId").references(Users) { it.owner }
    val private = boolean("private").bindTo { it.private }
    val createdAt = date("createdAt").bindTo { it.createdAt }
    val updatedAt = date("updatedAt").bindTo { it.updatedAt }
}
