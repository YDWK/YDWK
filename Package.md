

# Module ydwk

A discord wrapper made in kotlin

## In progress and to be done
- [ ] Create entities - in progress
- [ ] Handle events - In progress
- [ ] Handle reconnect and resuming
- [ ] Handle rate limiting in websocket

## Future Features
- [ ] Handle slash commands
- [ ] Handle sending events

## Implemented
- [x] Connect to gateway
- [x] Parse json
- [x] Handle all op codes
- [x] Caching
- [x] Handle Rest API



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

    ydwk.on<ReadyEvent> { println("Ready!") }
}
```