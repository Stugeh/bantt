package com.bantt.services

import com.bantt.models.Server
import com.bantt.models.User
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.json
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.regex
import java.util.*

val _dbClient = KMongo.createClient(dotenv()["MONGO_URI"]).coroutine
val _database = _dbClient.getDatabase("Bantt")
val _userCollection = _database.getCollection<User>()
val _serverCollection = _database.getCollection<Server>()

/* Generic  methods*/
fun <T : Any> CoroutineCollection<T>.getAll(): String = this.find().json

fun <T : Any> CoroutineCollection<T>.getById(id: UUID): String? =
    runBlocking { this@getById.findOneById(id.toString())?.json }

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

fun CoroutineCollection<User>.getByUsername(username: String): User? =
    runBlocking {
        this@getByUsername.findOne(User::username eq username)
    }

/* Channel specific methods */
fun CoroutineCollection<Server>.getChannelsByName(name: String): List<Server> =
    runBlocking { this@getChannelsByName.find(Server::name regex "/$name/").toList() }
