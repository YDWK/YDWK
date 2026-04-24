# YDWK test style guide

This module follows a Discord-wrapper oriented unit test style so gateway payload, entity mapping, and builder behavior stay readable and stable.

## Core rules

- Name tests by behavior (`maps_ready_gateway_payload_to_ready_event_enum`).
- Keep tests deterministic: no network calls, no sleeps, no clock assumptions unless injected.
- Assert JSON semantically (JSON tree equality), not by whitespace-sensitive strings.
- Prefer fixture-driven tests for Discord payloads under `src/test/resources/jsons`.
- Keep one behavior per test; use clear Given/When/Then flow.

## Utilities

- `io.github.ydwk.ydwk.testkit.DiscordJsonFixtures`: reads payload fixtures from resources.
- `io.github.ydwk.ydwk.testkit.assertJsonEquals`: compares JSON by structure, not formatting.

## Recommended structure

- Gateway/event routing tests: `io.github.ydwk.ydwk.ws`
- Entity and cache tests: `io.github.ydwk.ydwk.cache`
- Builders and payload generation tests: `io.github.ydwk.ydwk.component`, `io.github.ydwk.ydwk.embed`, `io.github.ydwk.ydwk.slash`

## Running tests

```zsh
./gradlew :ydwk:test
```

