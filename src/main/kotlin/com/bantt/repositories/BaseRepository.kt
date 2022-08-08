package com.bantt.repositories

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.json
import java.util.*

abstract class BaseRepository<T : Any> {
    abstract val collection: CoroutineCollection<T>

    fun getAll(): String = collection.find().json

    suspend fun get(id: UUID): String? = collection.findOneById(id.toString())?.json

    suspend fun save(body: String): Boolean = try {
        collection.insertOne(body.toModel())
        true
    } catch (e: Exception) {
        println("request save failed: $e")
        false
    }

    suspend fun update(id: UUID, body: String): String? = try {
        collection.updateOneById(id.toString(), body.toModel())
        get(id)
    } catch (e: Exception) {
        println("request update failed: $e")
        null
    }

    suspend fun delete(id: String): Boolean = try {
        collection.deleteOneById(ObjectId(id))
        true
    } catch (e: Exception) {
        println("request delete failed: $e")
        false
    }

    protected fun Any?.toJson() = toGson(this)

    private fun <R> toGson(r: R): String = GsonBuilder().setPrettyPrinting().create().toJson(r)

    private fun String?.toModel(): T = Gson().fromJson(this)

    private fun Gson.fromJson(json: String?): T = this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}
