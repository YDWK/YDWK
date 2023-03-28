![](https://github.com/YDWK/YDWK/blob/master/ydwk-s.png)
<br>
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-1.8.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![codecov](https://codecov.io/gh/YDWK/YDWK/branch/master/graph/badge.svg?token=LKIA8T6N6J)](https://codecov.io/gh/YDWK/YDWK)
[![ydwk](https://img.shields.io/badge/YDWK--Version-v1.6.05-blue)](https://github.com/YDWK/YDWK/releases/tag/v1.6.0)
[![ydwk-docs](https://img.shields.io/badge/YDWK-Docs-blue.svg)](https://www.ydwk.org)


# Very important note
Don't update to 1.22.0 it was a mistaken release caused by a bug in the code, use the latest version found above.

# YDWK

YDWK is a discord wrapper made in Kotlin that aims to be as simple and as fast as possible while still being easy to use and understand. It is built to be similar to libaries like discord.js but is different in its own way. It tries to adhere to the [discord Api](https://discord.com/developers/docs/intro) as much as possible.

## Getting started

## :package: Installation

### Grovy DLS gradle
```groovy
repositories {
    mavenCentral()
}
dependencies {
    implementation "io.github.realyusufismail:ydwk:${project.version}"
}
```

### Kotlin DLS gradle
```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("io.github.realyusufismail:ydwk:${project.version}")
}
```

### Maven
```xml
<dependency>
    <groupId>io.github.realyusufismail</groupId>
    <artifactId>ydwk</artifactId>
    <version>${project.version}</version>
</dependency>
```
  </CodeGroupItem>
</CodeGroup>


Now to create a default bot, add the following to your main class:

```kotlin
fun main() {
    createDefaultBot("TOKEN").build()
}
```

A default bot has all the recommended gateway intents.

For more details see the [creating a basic bot](https://www.ydwk.org/docs/tutorial/basicbot.html)

## Getting cached entities and getting entities through the rest api

When you want to get a cached entity, it will be named `getEntity` and when you want to get an entity through the Rest
API, it will be named `requestEntity`.

## Features : 

### In progress and to be done

### Future Features

### Implemented

- [x] Handle Rest API
- [x] Connect to gateway
- [x] Parse Json
- [x] Handle all op codes
- [x] Caching
- [x] Handle reconnect and resuming
- [x] Support for intents
- [x] Handle slash commands
- [x] Reply system for slash commands
- [x] Embed builder
- [x] Support for messages
- [x] Handle rate limiting in Websocket
- [x] Handle rate limiting in rest
- [x] Support for channels
- [x] Create entities - in progress
- [x] Handle events - In progress

## For more information

Check out the [docs](https://www.ydwk.org/) for more information.
