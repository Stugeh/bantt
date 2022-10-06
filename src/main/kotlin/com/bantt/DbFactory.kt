package com.bantt

import com.bantt.models.*
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDb() {
    val db = Database.connect(dotenv()["DEV_DB"], "org.postgresql.Driver")
    transaction {
        SchemaUtils.create(Channels)
        SchemaUtils.create(Users)
        SchemaUtils.create(Servers)
        SchemaUtils.create(Subscriptions)
        SchemaUtils.create(Messages)
    }
}

