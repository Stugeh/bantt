package com.bantt.dl

import com.bantt.services.ChannelService
import com.bantt.services.UserService
import io.github.cdimascio.dotenv.dotenv
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val dbClient = KMongo.createClient(dotenv()["MONGO_URI"]).coroutine

val db = dbClient.getDatabase("Bantt")

val mainModule = module(createdAtStart = true) {
    single { db }
}

val serviceModule = module(createdAtStart = true) {
    single { UserService() }
    single { ChannelService() }
}
