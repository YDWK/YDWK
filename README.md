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

## Bot templates

Ready-to-copy bot templates with pluggable database support (SQLite / PostgreSQL / MongoDB):

| Template | Description |
|---|---|
| [Moderation bot](examples/moderation-bot.md) | warn, ban, auto-ban at warn threshold, warn records in DB |
| [Ticket bot](examples/ticket-bot.md) | panel-style ticket open/close, transcript-ready, per-guild DB |

Each template separates the database layer into a single file so you can swap the backend by
changing one `Database.connect(...)` call.

## Documentation

See the full docs at [ydwk.org](https://www.ydwk.org/).

## Releasing to Maven Central

OSSRH (s01.oss.sonatype.org) was shut down in June 2025. Publishing now goes through
**Sonatype Central Portal** (`central.sonatype.com`) using the `com.gradleup.nmcp` plugin.

### One-time setup

1. Create an account at [central.sonatype.com](https://central.sonatype.com).
2. Claim the namespace `io.github.realyusufismail` (verify via GitHub).
3. Generate a **user token** (Account → Generate Token).
4. Add the credentials to `~/.gradle/gradle.properties` (never commit these):

```properties
mavenUsername=<token-username>
mavenToken=<token-password>
```

5. Set up GPG signing keys and add them to `~/.gradle/gradle.properties`:

```properties
signing.keyId=<last-8-chars-of-key-id>
signing.password=<key-passphrase>
signing.secretKeyRingFile=/path/to/secring.gpg
```

### Publishing a release

```bash
# 1. Ensure version in gradle.properties does NOT end with -SNAPSHOT
#    e.g.  version = 2.0.0-alpha.2

# 2. Build and sign all subproject artifacts into their local staging dirs
./gradlew :yde-api:publishYde-apiPublicationToStagingDirRepository \
          :yde-impl:publishYde-implPublicationToStagingDirRepository \
          :ydwk:publishYdwkPublicationToStagingDirRepository

# 3. Bundle and upload everything to Central Portal in one shot
./gradlew publishAllPublicationsToCentralPortal
```

With `publicationType = "AUTOMATIC"` (the default in this project) the bundle is released
automatically. Switch to `"USER_MANAGED"` if you want to review the deployment on the portal
before it goes live.

> **Snapshot releases** — Central Portal does not support snapshots the same way OSSRH did.
> For snapshot testing use a local Maven repo (`mavenLocal()`) or GitHub Packages.
