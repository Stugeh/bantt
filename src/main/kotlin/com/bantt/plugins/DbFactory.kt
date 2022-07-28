package com.bantt.plugins

import com.bantt.models.Channel
import com.bantt.models.User
import io.github.cdimascio.dotenv.dotenv
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object DbFactory {
    fun init() {
        val client = KMongo.createClient(dotenv()["MONGO_URI"]).coroutine
        val db = client.getDatabase("bantt")
        val users = db.getCollection<User>()
        val channels = db.getCollection<Channel>()
    }
}

