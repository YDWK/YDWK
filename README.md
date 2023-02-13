![](https://github.com/RealYusufIsmail/YDWK/blob/master/ydwk-s.png)
<br>
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![codecov](https://codecov.io/gh/YDWK/YDWK/branch/master/graph/badge.svg?token=LKIA8T6N6J)](https://codecov.io/gh/YDWK/YDWK)
[![ydwk](https://img.shields.io/badge/YDWK--Version-v1.0.5-blue)](https://github.com/YDWK/YDWK/releases/tag/v1.0.5)
[![ydwk-docs](https://img.shields.io/badge/YDWK-Docs-blue.svg)](https://www.ydwk.org)


# Very important note
Don't update to 1.22.0 it was a mistake release caused by a bug in the code, use the latest version found above.

# YDWK

YDWK is a discord wrapper made in kotlin that aims to be as simple and as fast as possible while still being easy to use and understand. It is built to be similar to discord.js but with some differences. It tries to adhere to the [discord api](https://discord.com/developers/docs/intro) as much as possible.

## In progress and to be done

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
- [x] Create entities - in progress
- [x] Handle events - In progress

## Getting started

Add the following to your `build.gradle.kts`:

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

### Logging system

//TODO

## For more information

Check out the [docs](https://www.ydwk.org/) for more information.
