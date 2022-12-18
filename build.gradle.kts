val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val kmongoVersion: String by project
val koinVersion: String by project

plugins {
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.2.1"
}

group = "com.bantt"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-locations-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-client-apache-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-hsts-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-http-redirect-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.0")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongoVersion")
    implementation("com.google.code.gson:gson:2.10")
    implementation("org.mindrot:jbcrypt:0.4")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}
