package com.bantt.models

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.uuid
import java.util.*

interface Subscription : Entity<Subscription> {
    val id: UUID
    val user: User
    val channel: Channel
}

object Subscriptions : Table<Subscription>("t_subscription") {
    val id = uuid("id").bindTo { it.id }
    val userId = uuid("userId").references(Users) { it.user }
    val channelId = uuid("channelId").references(Channels) { it.channel }
}
