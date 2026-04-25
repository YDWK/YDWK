# Moderation Bot Template

A complete template for a moderation bot using YDWK, with a pluggable database layer for storing
warn/ban records.

## Gradle dependencies

```kotlin
// build.gradle.kts
dependencies {
    implementation("io.github.realyusufismail:ydwk:<version>")

    // --- Pick ONE database option ---

    // Option A: SQLite (zero-config, good for small bots)
    implementation("org.jetbrains.exposed:exposed-core:0.61.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.61.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.61.0")
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")

    // Option B: PostgreSQL (production-grade)
    // implementation("org.jetbrains.exposed:exposed-core:0.61.0")
    // implementation("org.jetbrains.exposed:exposed-dao:0.61.0")
    // implementation("org.jetbrains.exposed:exposed-jdbc:0.61.0")
    // implementation("org.postgresql:postgresql:42.7.5")

    // Option C: MongoDB (document store)
    // implementation("org.litote.kmongo:kmongo-coroutine:4.11.0")
}
```

## Project layout

```
src/main/kotlin/
  Main.kt
  db/
    Database.kt        <- database setup (swap this for your DB of choice)
    WarnRepository.kt  <- CRUD for warnings
  listeners/
    ModerationListener.kt
  commands/
    WarnCommand.kt
    BanCommand.kt
    ClearCommand.kt
```

## Database layer (Exposed + SQLite)

```kotlin
// db/Database.kt
package com.example.bot.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object Warnings : Table("warnings") {
    val id        = integer("id").autoIncrement()
    val guildId   = varchar("guild_id", 20)
    val userId    = varchar("user_id", 20)
    val reason    = text("reason")
    val moderator = varchar("moderator_id", 20)
    val timestamp = long("timestamp")
    override val primaryKey = PrimaryKey(id)
}

fun initDatabase(path: String = "bot.db") {
    Database.connect("jdbc:sqlite:$path", driver = "org.sqlite.JDBC")
    transaction { SchemaUtils.create(Warnings) }
}
```

```kotlin
// db/WarnRepository.kt
package com.example.bot.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object WarnRepository {

    fun addWarn(guildId: String, userId: String, reason: String, moderatorId: String): Int =
        transaction {
            Warnings.insertAndGetId {
                it[Warnings.guildId]   = guildId
                it[Warnings.userId]    = userId
                it[Warnings.reason]    = reason
                it[Warnings.moderator] = moderatorId
                it[Warnings.timestamp] = System.currentTimeMillis()
            }.value
        }

    fun getWarns(guildId: String, userId: String): List<ResultRow> =
        transaction {
            Warnings.selectAll()
                .where { (Warnings.guildId eq guildId) and (Warnings.userId eq userId) }
                .toList()
        }

    fun clearWarns(guildId: String, userId: String): Int =
        transaction {
            Warnings.deleteWhere { (Warnings.guildId eq guildId) and (Warnings.userId eq userId) }
        }
}
```

## Slash command registration

```kotlin
// commands/WarnCommand.kt
package com.example.bot.commands

import com.example.bot.db.WarnRepository
import io.github.ydwk.yde.interaction.slash.SlashCommandOption
import io.github.ydwk.yde.interaction.slash.SlashCommandOptionType
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent

suspend fun handleWarn(event: SlashCommandEvent) {
    val target    = event.getOption("user")?.asUser() ?: return event.reply("No user provided.").queue()
    val reason    = event.getOption("reason")?.asString() ?: "No reason provided"
    val guildId   = event.guild?.id?.asString() ?: return
    val modId     = event.user.id.asString()

    val warnCount = WarnRepository.addWarn(guildId, target.id.asString(), reason, modId)
    val total     = WarnRepository.getWarns(guildId, target.id.asString()).size

    event.reply("Warned ${target.username} | Reason: $reason | Total warns: $total").queue()

    // Auto-ban at 3 warns
    if (total >= 3) {
        event.guild?.ban(target, 0, "Reached $total warnings")?.queue()
        WarnRepository.clearWarns(guildId, target.id.asString())
    }
}
```

```kotlin
// commands/BanCommand.kt
package com.example.bot.commands

import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent

suspend fun handleBan(event: SlashCommandEvent) {
    val target = event.getOption("user")?.asUser() ?: return event.reply("No user provided.").queue()
    val reason = event.getOption("reason")?.asString() ?: "No reason provided"

    event.guild?.ban(target, 0, reason)?.queue {
        event.reply("Banned ${target.username} | Reason: $reason").queue()
    }
}
```

## Event listener

```kotlin
// listeners/ModerationListener.kt
package com.example.bot.listeners

import com.example.bot.commands.handleBan
import com.example.bot.commands.handleWarn
import io.github.ydwk.ydwk.evm.backend.event.CoroutineEventListener
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent

class ModerationListener : CoroutineEventListener {
    override suspend fun onEvent(event: GenericEvent) {
        if (event !is SlashCommandEvent) return
        when (event.slashCommandName) {
            "warn" -> handleWarn(event)
            "ban"  -> handleBan(event)
        }
    }
}
```

## Main entry point

```kotlin
// Main.kt
package com.example.bot

import com.example.bot.db.initDatabase
import com.example.bot.listeners.ModerationListener
import io.github.ydwk.ydwk.BotBuilder.Companion.buildBot
import io.github.ydwk.ydwk.GateWayIntent
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    initDatabase()  // creates bot.db in the working directory

    val ydwk = buildBot(System.getenv("BOT_TOKEN"))
        .intents(
            listOf(
                GateWayIntent.GUILDS,
                GateWayIntent.GUILD_MEMBERS,
                GateWayIntent.GUILD_MODERATION,
                GateWayIntent.GUILD_MESSAGES,
                GateWayIntent.MESSAGE_CONTENT,
            )
        )
        .build()
        .buildYDWK()

    ydwk.addEventListeners(ModerationListener())
    ydwk.awaitReady()
}
```

## Swapping to PostgreSQL

Only the `Database.connect(...)` call in `db/Database.kt` changes:

```kotlin
Database.connect(
    url      = "jdbc:postgresql://localhost:5432/modbot",
    driver   = "org.postgresql.Driver",
    user     = System.getenv("DB_USER"),
    password = System.getenv("DB_PASSWORD"),
)
```

Everything else (repositories, listeners) stays identical.
