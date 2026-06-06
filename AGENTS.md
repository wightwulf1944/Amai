# AGENTS.md

Guidance for AI agents working in this repository. The goal is to reduce repeat discovery and make changes that fit the existing Android app.

This document is a researched map, not a replacement for reading the code. Facts below include source files wherever practical; if a file disagrees with this guide, trust the file and update this guide as part of the change. Inform the user of any mismatch.

## Table of Contents

1. Source Of Truth
2. Project Snapshot
3. Local Environment Notes
4. Source Map
5. Runtime Flow
6. Data Model And Storage
7. Network
8. UI Conventions
9. ViewModel And Reactive Patterns
10. Known Code Quirks
11. Editing Guidelines
12. Verification

## Source Of Truth

Start with these files when validating project facts:

- **Build & Dependencies**: `settings.gradle`, `app/build.gradle`, `gradle.properties`.
- **Runtime startup and DI**: `AmaiApplication.kt`, `koin/Modules.kt`.
- **Navigation & Screen Flow**: `MainActivity.kt`, `util/Navigation.kt`, `fragment/`, `compose/HomeScreen.kt`.
- **Room schema**: `data/AmaiDatabase.kt`, `app/schemas/`.
- **Network API**: `network/Nhentai.kt`, `util/NhentaiX.kt`.
- **Compose UI**: `compose/`.

## Project Snapshot

- **Project Name**: `Amai`.
- **Purpose**: Browse, search, view, favorite, and read galleries from `nhentai.net`.
- **UI Architecture**: Hybrid (XML layouts/ViewBinding + Jetpack Compose).
- **Tech Stack**: Room, Koin, RxJava 3, Retrofit, Coil.

## Local Environment Notes

- `local.properties` is machine-specific. Verify `sdk.dir` before using.
- Use Android Studio's JBR before running Gradle:
```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat :app:assembleDebug
```
- Git safe directory helper:
```powershell
git -c safe.directory=C:/android_projects/Amai status --short
```

## Source Map

- `MainActivity.kt`: chooses the initial fragment based on deep link, shared text, or normal home launch.
- `util/Navigation.kt`: Contains the manual fragment transaction logic. This app does **not** use Jetpack Navigation.
- `viewmodel/factory/ViewModelFactory.kt`: Manual factory combining Koin plus `SavedStateHandle`.
- `util/FragmentX.kt`: Provides `amaiViewModels<T>()` to wire fragments to the custom factory.
- `util/NhentaiX.kt`: Logic for mapping API DTOs to Room Entities.
- `widget/`: Contains custom UI components like `PageRecyclerView` (reader).

## Runtime Flow

- `MainActivity` hosts a single `FragmentContainerView`.
- First run opens `InitialSetupFragment` to select the storage path (`AmaiPreferences.storagePath`).
- Normal launch opens `HomeComposeFragment`, which hosts `HomeScreen` (Compose). This manages the browse and favorites tabs.
- `HomeScreen` uses `BrowseScreen` and `FavoritesScreen` Composables. It observes `NhentaiViewModel` and `FavoritesViewModel`.
- `ReadFragment` is XML-backed, using `PageRecyclerView` for the reader. It enters fullscreen while attached.

## Data Model And Storage

- **Database**: `AmaiDatabase` (Room).
- **Caching Pattern**: `NhentaiViewModel` stores remote browse/search results in the `CachedEntity` table. `CachedPreviewView` (a DatabaseView) then joins these results with local favorite state for display.
- **Persistence**: `FavoriteEntity` stores favorited book IDs.
- **Orphan Cleanup**: `BookDao.deleteOrphan()` is used to delete books no longer referenced by favorites or the current search cache.

## Network

- **API Documentation**: [Human-readable](https://nhentai.net/api/v2/docs) and [OpenAPI JSON](https://nhentai.net/api/v2/openapi.json).
- **Base URLs**: Defined in `network/Nhentai.kt`.
- **User-Agent**: Custom interceptor lives in `network/UserAgentInterceptor.kt`.
- **Debug Logging**: `HttpLoggingInterceptor` is enabled in debug builds via `Modules.kt`.
- **Capabilities & Limits**:
    - The `galleries/{id}` endpoint supports an `include` query parameter (comma-separated: `comments`, `related`, `favorite`, `suggestions`) to fetch extra data in one request. This currently not implemented.
    - A dedicated `/api/v2/galleries/popular` endpoint exists for trending content but is not yet implemented.
    - **Intentional Omissions**: Authentication (API Key) and security challenge handling (POW/Captcha) are currently bypassed/ignored by design.

## UI Conventions

- **Hybrid View**: Compose screens are embedded in fragments via `ComposeView` with `DisposeOnViewTreeLifecycleDestroyed`.
- **Theme**: Compose uses `AmaiTheme`. XML uses `Theme.Material3.Dark.NoActionBar`.
- **Grid**: Browse and detail layouts use adaptive grids with ~150 dp cells.
- **Resources**: Use existing strings in `res/values/strings.xml` and icons in `res/drawable` before adding new assets.

## ViewModel And Reactive Patterns

- **RxJava**: ViewModels use RxJava `Disposable` fields and explicit disposal in `onCleared()`.
- **Live Events**: `MainViewModel.searchEventLive` handles shared search events between fragments.
- **State**: `DetailViewModel` combines Room-backed Rx `Observable` state with separate remote refresh calls.

## Known Code Quirks

- `DetailViewModel.kt`: Uses unbounded `retry()` in `loadRemote()`.
- `!!` Usage: Many legacy areas use non-null assertions; verify `SavedStateHandle` or `Intent` extras carefully.
- `PageRecyclerView.java`: Handles volume-key page flips and tap zones. Changes here require manual testing on a device.

## Editing Guidelines

- **Respect user work**: Always run `git status --short` before editing.
- **Architecture**: Do not refactor from hybrid to pure Compose unless requested.
- **Room Migrations**: Increment `AmaiDatabase.version` and handle schema exports if changing entities.

## Verification

- **Lint**: Inspect `app/build/reports/lint-results-debug.html`.
- **Previews**: Use Compose `@Preview` where available.
- **Manual Flow**: Verify persistence and reader interactions manually, as there are no automated UI tests.
