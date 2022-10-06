package com.bantt.services

import com.bantt.models.*
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import java.util.*

val _database = Database.connect(dotenv()["DEV_DB"])
val Database.Subscriptions get() = this.sequenceOf(Subscriptions)

val x get() = Users

/* Generic  methods*/
fun <T : Any> CoroutineCollection<T>.getAll(): String = this.find().json

fun <E : Entity<E>> getById(id: UUID, table: Table<E>): Entity<E>? =
    runBlocking {
        println("herehere")
        sequenceOf(table).find { it.id eq id }
    }

fun <T : Any> CoroutineCollection<T>.save(body: T): Boolean = try {
    runBlocking { this@save.insertOne(body) }
    true
} catch (e: Exception) {
    println("request save failed: $e")
    false
}

fun <T : Any> CoroutineCollection<T>.update(id: UUID, body: T): String? = try {
    runBlocking { this@update.updateOneById(id.toString(), body) }
    getById(id)
} catch (e: Exception) {
    println("request update failed: $e")
    null
}

fun <T : Any> CoroutineCollection<T>.delete(id: String): Boolean = try {
    runBlocking { this@delete.deleteOneById(ObjectId(id)) }
    true
} catch (e: Exception) {
    println("request delete failed: $e")
    false
}

/* User specific methods */

fun CoroutineCollection<User>.getByUsername(username: String): User =
    runBlocking {
        this@getByUsername.findOne(User::username eq username)
    }

/* Channel specific methods */
fun CoroutineCollection<Channel>.getChannelsByName(name: String): List<Channel> =
    runBlocking { this@getChannelsByName.find(Channel::name regex "/$name/").toList() }
