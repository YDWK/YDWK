![YDWK](https://github.com/YDWK/YDWK/blob/master/ydwk-s.png)

[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-2.3.21-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![codecov](https://codecov.io/gh/YDWK/YDWK/branch/master/graph/badge.svg?token=LKIA8T6N6J)](https://codecov.io/gh/YDWK/YDWK)
[![Latest Release](https://img.shields.io/github/v/release/YDWK/YDWK)](https://github.com/YDWK/YDWK/releases)
[![Docs](https://img.shields.io/badge/YDWK-Docs-blue.svg)](https://www.ydwk.org)

# YDWK

YDWK is a Kotlin Discord wrapper focused on being simple, fast, and close to the [Discord API](https://discord.com/developers/docs/intro).

## Installation

Use the latest published version from [GitHub Releases](https://github.com/YDWK/YDWK/releases).

### Gradle (Groovy DSL)

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "io.github.realyusufismail:ydwk:<version>"
}
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.realyusufismail:ydwk:<version>")
}
```

### Maven

```xml
<dependency>
  <groupId>io.github.realyusufismail</groupId>
  <artifactId>ydwk</artifactId>
  <version><!-- latest --></version>
</dependency>
```

## Quick start

```kotlin
import io.github.ydwk.ydwk.BotBuilder.Companion.buildBot

fun main() {
    buildBot("TOKEN")
        .build()
        .buildYDWK()
}
```

`buildBot("TOKEN")` uses the recommended default gateway intents.

## Entity access pattern

- Cached entities use `get...` methods.
- REST-backed lookups use `request...` methods.

Example: `getGuildById(...)` vs `requestGuildById(...)`.

## Status

- Gateway connection and reconnect/resume support
- REST API support
- Slash commands and interaction replies
- Message and embed support
- Cache system
- Rate limit handling (gateway + REST)
- Voice support is actively evolving

## Documentation

See the full docs at [ydwk.org](https://www.ydwk.org/).
