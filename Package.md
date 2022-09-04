# Module YDWK

A discord wrapper made in kotlin

# Package io.github.realyusufismail.ydwk.entities

Contains all the discord entities such as guild, channel, user, etc

# Package io.github.realyusufismail.ydwk.events

Contains all the events that are emitted by the bot

# Package io.github.realyusufismail.ydwk.ws

Contains all the websocket related classes such as the gateway, the websocket, and the gateway intents

# Package io.github.realyusufismail.ydwk.impl

Contains all the implementations of the entities and events, not meant to be used by the user

## Level 2 Installation

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

A default bot has all the recommend gateway intents.

## Level 2 Getting started
Not yet implemented
