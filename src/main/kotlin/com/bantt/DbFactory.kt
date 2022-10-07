package com.bantt

import com.bantt.models.*
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDb() {
    val appConfig = HoconApplicationConfig(ConfigFactory.load())

    val dbUrl = dotenv()["DB_URL"]
    val dbUser = dotenv()["DB_USER"]
    val dbPassword = dotenv()["DB_PASSWORD"]

    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = dbUrl
    config.username = dbUser
    config.password = dbPassword
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()

    Database.connect(HikariDataSource(config))

    val flyway = Flyway.configure().dataSource(dbUrl, dbUser, dbPassword).load()
    flyway.migrate()

    transaction {
        SchemaUtils.create(Channels)
        SchemaUtils.create(Users)
        SchemaUtils.create(Servers)
        SchemaUtils.create(Subscriptions)
        SchemaUtils.create(Messages)
    }
}

suspend fun <T> dbQuery(block: () -> T): T =
    withContext(Dispatchers.IO) {
        transaction { block() }
    }
