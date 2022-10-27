# Module ydwk

A discord wrapper made in kotlin

## In progress and to be done

- [ ] Create entities - in progress
- [ ] Handle events - In progress

## Future Features

## Implemented

- [x] Handle Rest API
- [x] Connect to gateway
- [x] Parse json
- [x] Handle all op codes
- [x] Caching
- [x] Handle reconnect and resuming
- [x] Support for intents
- [x] Handle slash commands
- [x] Reply system for slash commands
- [x] Embed builder
- [x] Support for messages
- [x] Handle rate limiting in websocket
- [x] Handle rate limiting in rest
- [x] Support for channels

## Installation

Add the following to your `build.gradle`:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.realyusufismail:ydwk:${project.version}")
}
```

To create a default bot, add the following to your main class:

```kotlin
fun main() {
    createDefaultBot("TOKEN").build()
}
```

A default bot has all to recommend gateway intents.

## Getting started

To use an event, you can use the ListenerAdapter or inline method

```kotlin
class Bot : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Bot is ready!")
    }
}

fun main() {
    val ydwk = createDefaultBot(JConfigUtils.getString("token")).build()
    ydwk.addEvent(Bot())
}
```

or

```kotlin
fun main() {
    val ydwk = createDefaultBot(JConfigUtils.getString("token")).build()

    ydwk.on<ReadyEvent> { println("Ready!") }
}
```

## Getting cached entities and getting entities through the rest api

When you want to get a cached entity, it will be named `getEntity` and when you want to get an entity through the rest
api, it will be named `requestEntity`.