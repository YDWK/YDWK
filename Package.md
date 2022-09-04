

# Module ydwk

A discord wrapper made in kotlin

## In progress and to be done
- [x] Connect to gateway
- [x] Parse json
- [x] Handle all op codes
- [ ] Create entities - in progress
- [ ] Handle events - In progress
- [ ] Handle reconnect and resuming
- [ ] Handle rate limiting in websocket

## Future Features
- [ ] Handle slash commands
- [ ] Caching
- [ ] Handle Rest API
- [ ] Handle sending events


## Installation

Add the following to your `build.gradle`:

```gradle
dependencies {
    implementation("io.github.realyusufismail:ydwk:${project.version}")
}
```

To create a default bot, add the following to your main class:

```kotlin
fun main() {
    createDefaultBot("TOKEN")
}
```

A default bot has all to recommend gateway intents.

## Getting started

To use an event, add the following to your main class:

```kotlin
fun main() {
    val ydwk =
        createDefaultBot("TOKEN")

    //example
    ydwk.onEvent<ReadyEvent> { c -> println("Total amount of guilds: ${c.totalGuildsAmount}") }
}
```