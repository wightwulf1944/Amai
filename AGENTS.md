# AGENTS.md

Guidance for AI agents working in this repository.

## Mandatory Development Principles

- **Knowledge Persistence**: Whenever you make a mistake or learn a non-obvious project-specific fact, update this guide to avoid repeating the same error.
- **Verification First**: Always read files before attempting any edits.
- **Code is Truth**: The source code is the absolute authority. If it disagrees with this guide, update this guide to match the code.
- **UI Architecture**: Prefer Jetpack Compose over XML layouts and Fragments. Always consider adding previews whenever you create new composables.
- **Database integrity**: Whenever database schema changes are made, you MUST handle migrations.
- **Anti-Speculation**: Prioritize the current requirements and state of the app. Do not implement changes or choose architectures driven by "what-if" scenarios or future-proofing that are not required by the active task. Keeping the code optimized for its current state is preferred over speculative flexibility.
- **Tool Usage Policy**: 
  - **NEVER** use file-writing tools unless explicitly asked to "apply", "fix", or "go ahead". Use only read-only tools for investigations and explanations.
  - **DO NOT** use file-writing tools or propose code changes in that same turn unless explicitly asked to "fix", "refactor", "apply changes", or "go ahead".
  - **"How" ≠ "Do"**: Interpret "How do I..." or "Is it possible to..." as a request for a technical explanation or code snippets for discussion only. Never touch the filesystem or apply changes in response to these queries.
  - **Please investigate**: Interpret requests to investigate as a request for a technical explanation of an issue or code snippet. Never touch the filesystem or apply changes in response to these queries.
  - **Explain First, Edit Later**: Always provide a conceptual plan or code snippets in the chat for review before invoking any editing tools.
  - **Verification of Intent**: If unsure whether the user wants a conceptual answer or an implementation, ask for clarification.


## Project Overview

- **Project Name**: `Amai`
- **Purpose**: Browse, search, view, favorite, and read galleries from `nhentai.net`.
- **Primary Tech Stack**: Jetpack Compose, Jetpack Navigation 3, Room, Koin, Coroutines/Flow, Retrofit + Moshi, kotlinx.serialization, Coil, and Timber.

## Data Modeling & Naming Rules

- **DTOs**: Objects returned by Retrofit must end in `Dto`. (Defined in `data/remote/dto/`).
- **Entities**: Objects representing Room tables must end in `Entity`. (Defined in `data/local/entity/`).
- **Intermediates**: Room objects joining multiple tables must end in `Intermediate`. (Defined in `data/local/intermediate/`).
- **Domain Models**: Objects returned by repositories for UI consumption. They must NOT have a suffix (e.g., `BookDetail`, not `BookDetailModel`). (Defined in `model/`).
- **Callbacks**: Use `on<Property>Change` (e.g., `onSortChange`) instead of `on<Property>Changed` for lambda parameters, following Jetpack Compose naming conventions.
- **Repository Isolation**: Repositories must map DTOs/Entities into Domain Models. The UI layer should never see a DTO or Entity.

## Source Map (Source of Truth)

This document is a researched map, not a replacement for reading the code. Facts below include source files wherever practical. Use these files to validate project facts and understand the implementation:

- **Build & Dependencies**: `settings.gradle`, `app/build.gradle`, `gradle.properties`.
- **Runtime Startup & DI**: `AmaiApplication.kt`, `di/Modules.kt`.
- **Navigation & Flow**: `MainActivity.kt` (entry), `ui/navigation/Route.kt` (routes), `ui/HomeScreen.kt` (tabs), `ui/navigation/Navigator.kt`.
- **Screens**: Found in `ui/` (e.g., `ui/BrowseScreen.kt`, `ui/DetailScreen.kt`, `ui/ReadScreen.kt`).
- **Domain Models**: Found in `model/` (e.g., `BookDetail.kt`, `Page.kt`, `FavoritesSort.kt`).
- **Database & Local Data**: `data/local/AmaiDatabase.kt`, `data/local/dao/`, `data/local/entity/`, `data/local/intermediate/`.
- **Network & Remote Data**: `data/remote/Nhentai.kt`, `data/remote/dto/NhentaiDTO.kt`, `data/remote/UserAgentInterceptor.kt`.
- **Repositories**: `data/repository/`.
- **ViewModels**: `ui/viewmodel/`.
- **UI Components & Theme**: `ui/common/`, `ui/theme/`, `ui/image/` (Coil setup).
- **API Documentation**: [Human-readable docs](https://nhentai.net/api/v2/docs) and [OpenAPI JSON](https://nhentai.net/api/v2/openapi.json).

## Implementation Details & Known Quirks

### UI & UX
- **Side Effects & Navigation**: Use **callbacks** (not `LaunchedEffect`) for one-time UI reactions to user actions (e.g., scrolling to top after search/sort). `LaunchedEffect` re-triggers whenever a screen is re-composed during tab switching, leading to unintended "false-positive" events.
- **Pull-to-Refresh**: In `GalleryBody`, `PullToRefreshBox` is intentionally set to `isRefreshing = false` despite an active `isLoading` state. This avoids visual clutter when other loading indicators (like `LinearProgressIndicator`) are present in the same UI.

### Database
- **Development Migration**: `di/Modules.kt` is configured with `.fallbackToDestructiveMigration(dropAllTables = true)`, which resets the database on schema mismatches during development.

### Unimplemented & Omitted Features
- **Partial API Support**:
    - The `galleries/{id}` endpoint supports an `include` query parameter (comma-separated: `comments`, `related`, `favorite`, `suggestions`) which is not yet implemented.
    - The `/api/v2/galleries/popular` endpoint for trending content is not yet implemented.
- **Intentional Omissions**: Authentication (API Keys) and security challenge handling (POW/Captcha) are currently bypassed or ignored by design.
