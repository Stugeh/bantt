package com.bantt.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
