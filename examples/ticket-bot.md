# Ticket Bot Template

A template for a ticket/support system bot using YDWK, with a pluggable database layer for storing
ticket records and transcripts.

## Gradle dependencies

```kotlin
// build.gradle.kts
dependencies {
    implementation("io.github.realyusufismail:ydwk:<version>")

    // --- Pick ONE database option ---

    // Option A: SQLite (zero-config, good for small servers)
    implementation("org.jetbrains.exposed:exposed-core:0.61.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.61.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.61.0")
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")

    // Option B: PostgreSQL (recommended for multi-server bots)
    // implementation("org.jetbrains.exposed:exposed-core:0.61.0")
    // implementation("org.jetbrains.exposed:exposed-dao:0.61.0")
    // implementation("org.jetbrains.exposed:exposed-jdbc:0.61.0")
    // implementation("org.postgresql:postgresql:42.7.5")

    // Option C: MongoDB
    // implementation("org.litote.kmongo:kmongo-coroutine:4.11.0")
}
```

## Project layout

```
src/main/kotlin/
  Main.kt
  db/
    Database.kt
    TicketRepository.kt
  listeners/
    TicketListener.kt     <- button clicks and slash command
  commands/
    TicketCommand.kt      <- /ticket open|close|transcript
```

## Database layer (Exposed + SQLite)

```kotlin
// db/Database.kt
package com.example.bot.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object Tickets : Table("tickets") {
    val id          = integer("id").autoIncrement()
    val guildId     = varchar("guild_id", 20)
    val channelId   = varchar("channel_id", 20)
    val ownerId     = varchar("owner_id", 20)
    val status      = varchar("status", 10).default("OPEN") // OPEN | CLOSED
    val createdAt   = long("created_at")
    val closedAt    = long("closed_at").nullable()
    override val primaryKey = PrimaryKey(id)
}

fun initDatabase(path: String = "tickets.db") {
    Database.connect("jdbc:sqlite:$path", driver = "org.sqlite.JDBC")
    transaction { SchemaUtils.create(Tickets) }
}
```

```kotlin
// db/TicketRepository.kt
package com.example.bot.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object TicketRepository {

    fun openTicket(guildId: String, channelId: String, ownerId: String): Int =
        transaction {
            Tickets.insertAndGetId {
                it[Tickets.guildId]   = guildId
                it[Tickets.channelId] = channelId
                it[Tickets.ownerId]   = ownerId
                it[Tickets.createdAt] = System.currentTimeMillis()
            }.value
        }

    fun closeTicket(channelId: String) =
        transaction {
            Tickets.update({ Tickets.channelId eq channelId }) {
                it[status]   = "CLOSED"
                it[closedAt] = System.currentTimeMillis()
            }
        }

    fun getByChannel(channelId: String): ResultRow? =
        transaction {
            Tickets.selectAll()
                .where { Tickets.channelId eq channelId }
                .firstOrNull()
        }

    fun getOpenTicketsFor(guildId: String, userId: String): List<ResultRow> =
        transaction {
            Tickets.selectAll()
                .where {
                    (Tickets.guildId eq guildId) and
                    (Tickets.ownerId eq userId) and
                    (Tickets.status eq "OPEN")
                }
                .toList()
        }
}
```

## Slash command handler

```kotlin
// commands/TicketCommand.kt
package com.example.bot.commands

import com.example.bot.db.TicketRepository
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent

suspend fun handleTicket(event: SlashCommandEvent) {
    val guild = event.guild ?: return
    val sub   = event.getSubCommandName()

    when (sub) {
        "open" -> {
            val existing = TicketRepository.getOpenTicketsFor(
                guild.id.asString(), event.user.id.asString()
            )
            if (existing.isNotEmpty()) {
                event.reply("You already have an open ticket.").setEphemeral(true).queue()
                return
            }

            // Create a private channel visible only to the user + staff role
            val channel = guild.createTextChannel("ticket-${event.user.username}") {
                // set permissions here via PermissionOverwrite
            }.queue()

            // channel id comes back asynchronously — store after creation
            event.reply("Ticket created!").setEphemeral(true).queue()
        }

        "close" -> {
            val channelId = event.channel?.id?.asString() ?: return
            val ticket    = TicketRepository.getByChannel(channelId)
            if (ticket == null) {
                event.reply("This channel is not a ticket.").setEphemeral(true).queue()
                return
            }
            TicketRepository.closeTicket(channelId)
            event.reply("Ticket closed. Channel will be deleted in 5 seconds.").queue()
            // delete channel after delay
            kotlinx.coroutines.delay(5_000)
            event.channel?.delete()?.queue()
        }
    }
}
```

## Button-click listener

Tickets often use an embed with a button to open a new ticket (panel style):

```kotlin
// listeners/TicketListener.kt
package com.example.bot.listeners

import com.example.bot.commands.handleTicket
import io.github.ydwk.ydwk.evm.backend.event.CoroutineEventListener
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.button.ButtonClickEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent

class TicketListener : CoroutineEventListener {
    override suspend fun onEvent(event: GenericEvent) {
        when (event) {
            is SlashCommandEvent -> {
                if (event.slashCommandName == "ticket") handleTicket(event)
            }

            is ButtonClickEvent -> {
                if (event.componentId == "open_ticket") {
                    // reuse close/open logic via a synthetic slash-like handler,
                    // or inline the channel creation here
                    event.reply("Opening your ticket...").setEphemeral(true).queue()
                }
            }
        }
    }
}
```

## Main entry point

```kotlin
// Main.kt
package com.example.bot

import com.example.bot.db.initDatabase
import com.example.bot.listeners.TicketListener
import io.github.ydwk.ydwk.BotBuilder.Companion.buildBot
import io.github.ydwk.ydwk.GateWayIntent
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    initDatabase()

    val ydwk = buildBot(System.getenv("BOT_TOKEN"))
        .intents(
            listOf(
                GateWayIntent.GUILDS,
                GateWayIntent.GUILD_MEMBERS,
                GateWayIntent.GUILD_MESSAGES,
                GateWayIntent.MESSAGE_CONTENT,
            )
        )
        .build()
        .buildYDWK()

    ydwk.addEventListeners(TicketListener())
    ydwk.awaitReady()
}
```

## Swapping to PostgreSQL

Only the `Database.connect(...)` call in `db/Database.kt` changes:

```kotlin
Database.connect(
    url      = "jdbc:postgresql://localhost:5432/ticketbot",
    driver   = "org.postgresql.Driver",
    user     = System.getenv("DB_USER"),
    password = System.getenv("DB_PASSWORD"),
)
```

All repositories and listeners stay exactly the same.
