[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.10-blue.svg?logo=kotlin)](http://kotlinlang.org)

# YDWK
A discord wrapper made in kotlin(Not related to YDW)

## Problems this wrapper solves
You might ask why should i use this wrapper when there is already a kotlin wrapper out there. Well, this wrapper is made to solve the following problems:
- I will make sure there little to no memory leaks
- I have contributed to other libraries and created my own library in java which means i have a lot of experience on how to make a good wrapper
- This wrapper aims to be as simple and as fast as possible while still being easy to use and understand
- This is built to be similar to discord.js

## In progress and to be done
- [x] Connect to gateway
- [x] Parse json
- [ ] Handle all op codes - in progress
- [ ] Handle events
- [ ] Handle reconnect and resuming
- [ ] Handle rate limiting in websocket

## Future Features
- [ ] Handle slash commands
- [ ] Caching
- [ ] Create entities
- [ ] Handle Rest API


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
    createDefaultBot("TOKEN")
}
```

A default bot has all the recommend gateway intents.