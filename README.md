![](https://github.com/RealYusufIsmail/YDWK/blob/master/ydwk.png)
<br>
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![codecov](https://codecov.io/gh/YDWK/YDWK/branch/master/graph/badge.svg?token=LKIA8T6N6J)](https://codecov.io/gh/YDWK/YDWK)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.realyusufismail/ydwk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.realyusufismail/ydwk)

# YDWK

A discord wrapper made in kotlin(Not related to YDW)

## Problems this wrapper solves

You might ask why should i use this wrapper when there is already a kotlin wrapper out there. Well, this wrapper is made
to solve the following problems:

- This wrapper aims to be as simple and as fast as possible while still being easy to use and understand
- This is built to be similar to discord.js
- I will stick to the same naming as in the discord api for consistency

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

## Getting started

Add the following to your `build.gradle`:

```gradle
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

A default bot has all the recommend gateway intents.

## Getting cached entities and getting entities through the rest api

When you want to get a cached entity, it will be named `getEntity` and when you want to get an entity through the rest
api, it will be named `requestEntity`.

