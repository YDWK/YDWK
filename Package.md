

# Module ydwk

A discord wrapper made in kotlin

## In progress and to be done
- [ ] Create entities - in progress
- [ ] Handle events - In progress
- [ ] Handle rate limiting in websocket

## Future Features
- [ ] Handle slash commands

## Implemented
- [x] Connect to gateway
- [x] Parse json
- [x] Handle all op codes
- [x] Caching
- [x] Handle Rest API
- [x] Handle reconnect and resuming



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
    createDefaultBot("TOKEN")
}
```

A default bot has all to recommend gateway intents.

## Getting started

To use an event, you can use the ListenerAdapter or inline method

```kotlin
object Bot : ListenerAdapter() {

    @JvmStatic
    fun main(args: Array<String>) {
        val ydwk =
            createDefaultBot(JConfigUtils.getString("token"))

        ydwk.addEvent(this)
    }

    override fun onReady(event : ReadyEvent) {
        println("Bot is ready!")
    }
}
```

or 

```kotlin
object Bot {
    @JvmStatic
    fun main(args: Array<String>) {
        val ydwk =
            createDefaultBot(JConfigUtils.getString("token"))

        ydwk.on<ReadyEvent> {
            println("Ready!")
        }
    }
}
```