# AGENTS.md

Guidance for AI agents working in this repository.

## Mandatory Development Principles

- **Knowledge Persistence**: Whenever you make a mistake or learn a non-obvious project-specific fact, update this guide to avoid repeating the same error.
- **Verification First**: Always read files before attempting any edits.
- **Consistency**: If a file disagrees with this guide, trust the file and update this guide as part of the change. Inform the user of any mismatch.
- **UI Architecture**: Prefer Jetpack Compose over XML layouts and Fragments. Always consider adding previews whenever you create new composables.
- **Database integrity**: Whenever database schema changes are made, you MUST handle migrations.
- **Communication Style**: If the user asks a question or for a "check," answer it clearly. **DO NOT** use file-writing tools or propose code changes in that same turn unless explicitly asked to "fix", "refactor", or "apply changes". Wait for confirmation.

## Project Overview

- **Project Name**: `Amai`
- **Purpose**: Browse, search, view, favorite, and read galleries from `nhentai.net`.
- **Primary Tech Stack**: Jetpack Compose, Jetpack Navigation 3, Room, Koin, Coroutines/Flow, Retrofit + Moshi, kotlinx.serialization, Coil, and Timber.

## Source Map (Source of Truth)

This document is a researched map, not a replacement for reading the code. Facts below include source files wherever practical. Use these files to validate project facts and understand the implementation:

- **Build & Dependencies**: `settings.gradle`, `app/build.gradle`, `gradle.properties`.
- **Runtime Startup & DI**: `AmaiApplication.kt`, `koin/Modules.kt`.
- **Navigation & Flow**: `MainActivity.kt` (root navigation), `HomeScreen.kt` (tab management).
- **Screens**: `BrowseScreen.kt`, `FavoritesScreen.kt`, `DetailScreen.kt`, `ReadScreen.kt`.
- **Database & Schema**: `data/AmaiDatabase.kt` and `app/schemas/`.
- **Network & API**: `network/Nhentai.kt`, `util/NhentaiX.kt`.
- **API Documentation**: [Human-readable docs](https://nhentai.net/api/v2/docs) and [OpenAPI JSON](https://nhentai.net/api/v2/openapi.json).

## Implementation Details & Known Quirks

### State Management
- **Tab State Preservation**: `HomeScreen` uses `SaveableStateHolder` to preserve tab state. Note that this **only** saves values that use `rememberSaveable`. Simple `remember` values are lost when switching tabs.
- **Search Event Handling**: `MainActivity` holds a global `searchEvent` state. This is prop-drilled down through `HomeScreen` to `BrowseScreen`, which uses a `LaunchedEffect(searchEvent)` to trigger ViewModel actions.
- **Side Effects & Navigation**: Use **callbacks** (not `LaunchedEffect`) for one-time UI reactions to user actions (e.g., scrolling to top after search/sort). `LaunchedEffect` re-triggers whenever a screen is re-composed during tab switching, leading to unintended "false-positive" events.

### Database
- **Development Migration**: `Modules.kt` is configured with `.fallbackToDestructiveMigration(dropAllTables = true)`, which resets the database on schema mismatches during development.

### Unimplemented & Omitted Features
- **Partial API Support**:
    - The `galleries/{id}` endpoint supports an `include` query parameter (comma-separated: `comments`, `related`, `favorite`, `suggestions`) which is not yet implemented.
    - The `/api/v2/galleries/popular` endpoint for trending content is not yet implemented.
- **Intentional Omissions**: Authentication (API Keys) and security challenge handling (POW/Captcha) are currently bypassed or ignored by design.
