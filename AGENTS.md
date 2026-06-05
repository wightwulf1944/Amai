# AGENTS.md

Guidance for AI agents working in this repository. The goal is to reduce repeat discovery and make changes that fit the existing Android app.

This document is a researched map, not a replacement for reading the code. Facts below include source files wherever practical; if a file disagrees with this guide, trust the file and update this guide as part of the change. Inform the user of any mismatch.

## Table of Contents

1. Source Of Truth
2. Project Snapshot
3. Build And Tooling
4. Local Environment Notes
5. Source Map
6. Runtime Flow
7. Data Model And Storage
8. Network
9. UI Conventions
10. ViewModel And Reactive Patterns
11. Known Code Quirks
12. Editing Guidelines
13. Verification
14. Common Search Patterns

## Source Of Truth

Start with these files when validating project facts:

- **Build, SDK, plugins, dependencies, module list**: `settings.gradle`, `build.gradle`, `app/build.gradle`, `gradle/wrapper/gradle-wrapper.properties`, `gradle.properties`.
- **App identity, entry points, permissions, deep links**: `app/build.gradle`, `app/src/main/AndroidManifest.xml`.
- **Runtime startup and dependency injection**: `app/src/main/java/i/am/shiro/amai/AmaiApplication.kt`, `app/src/main/java/i/am/shiro/amai/koin/Modules.kt`.
- **Navigation and screen flow**: `app/src/main/java/i/am/shiro/amai/MainActivity.kt`, `app/src/main/java/i/am/shiro/amai/util/Navigation.kt`, files under `app/src/main/java/i/am/shiro/amai/fragment`.
- **Room schema and persistence**: `app/src/main/java/i/am/shiro/amai/data/AmaiDatabase.kt`, files under `app/src/main/java/i/am/shiro/amai/data`, exported JSON under `app/schemas/i.am.shiro.amai.data.AmaiDatabase`.
- **Network API**: `app/src/main/java/i/am/shiro/amai/network/Nhentai.kt`, `app/src/main/java/i/am/shiro/amai/network/UserAgentInterceptor.kt`, `app/src/main/java/i/am/shiro/amai/util/NhentaiX.kt`.
- **Compose UI and theme**: files under `app/src/main/java/i/am/shiro/amai/compose`, plus XML theme/colors in `app/src/main/res/values`.
- **Generated or environment-specific state**: `local.properties`, `app/build/reports`, `app/build/outputs`, and Git branch/status. Re-check these on every session.

## Project Snapshot

- **Project Name**: `Amai`.
- **Purpose**: Browse, search, view, favorite, and read galleries. Support for `nhentai.net` deep links (`https://nhentai.net/g/...`) and text-share intents.
- **UI Architecture**: Hybrid (XML layouts/ViewBinding + Jetpack Compose).
- **Tech Stack**:
    - **Persistence**: Room database (`amai`).
    - **Dependency Injection**: Koin.
    - **Reactive Data Flow**: RxJava 3 with LiveData bridges.
    - **Networking**: Retrofit + OkHttp + Moshi.
    - **Image Loading**: Coil.
- **Environment**: Repository root is `C:\android_projects\Amai`.

## Build And Tooling

- `gradle/wrapper/gradle-wrapper.properties`
    - Gradle distribution version and wrapper configuration.
- `settings.gradle`
    - List of included modules (e.g., `:app`).
    - Repository definitions for plugins and dependencies.
- `build.gradle` (Project Root)
    - Global plugin definitions and their versions (AGP, Kotlin, KSP, Room, etc.).
- `app/build.gradle`
    - Android SDK configuration (`compileSdk`, `minSdk`, `targetSdk`).
    - App identity (`namespace`, `applicationId`).
    - Versioning metadata (`versionCode`, `versionName`).
    - Java toolchain requirements (`languageVersion`).
    - Build features state (`viewBinding`, `buildConfig`, `compose`).
    - Room schema export directory.
    - Full list of implementation, debug, and KSP dependencies.
- `gradle.properties`
    - Project-wide settings and JVM memory configuration.
- **Note on Firebase/Google Services**: Plugins are often present but commented out in Gradle files. `google-services.json` may exist in the tree.
- **Note on .gitignore**: `.idea/`, `.gradle/`, `build/`, and `local.properties` are ignored.

## Local Environment Notes

- `local.properties` is intentionally ignored and is machine-specific. Verify `sdk.dir` before using.
- Verify installed SDKs and build-tools against the requirements in `app/build.gradle`.
- `java` on PATH may be unreliable. Use Android Studio's JBR before running Gradle:

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat :app:assembleDebug
```

- If Git reports dubious ownership, use a per-command safe directory instead of changing global config unless the user asks:

```powershell
git -c safe.directory=C:/android_projects/Amai status --short
```

## Source Map

- `app/src/main/AndroidManifest.xml`: entry points, permissions, deep links, and shared text intents. Note: `MainActivity` is locked to portrait.
- `app/src/main/java/i/am/shiro/amai/AmaiApplication.kt`: starts Koin, installs Timber debug logging, creates the default notification channel, and provides Coil's app-wide `ImageLoader`.
- `app/src/main/java/i/am/shiro/amai/MainActivity.kt`: chooses the initial fragment based on deep link, shared text, first-run preference, or normal home launch.
- `app/src/main/java/i/am/shiro/amai/koin/Modules.kt`: Koin module for Room database, preferences, OkHttp, Moshi, and Retrofit API.
- `app/src/main/java/i/am/shiro/amai/network/Nhentai.kt`: Retrofit API, API/image base URLs, sort enum, and response DTOs.
- `app/src/main/java/i/am/shiro/amai/data`: Room database, DAOs, entities, relation intermediates, and database views.
- `app/src/main/java/i/am/shiro/amai/viewmodel`: app ViewModels. Shared factory lives in `viewmodel/factory/ViewModelFactory.kt`.
- `app/src/main/java/i/am/shiro/amai/fragment`: fragment entry points, including XML-backed screens, Compose-backed screens, and dialogs.
- `app/src/main/java/i/am/shiro/amai/compose`: Compose UI for browse and detail screens plus Material 3 theme/colors.
- `app/src/main/java/i/am/shiro/amai/adapter`: legacy RecyclerView adapters for favorites previews, reading pages, and search suggestions.
- `app/src/main/java/i/am/shiro/amai/widget`: custom widgets and behaviors, including `PageRecyclerView`, `SearchInput`, and `TopFloatingBehavior`.
- `app/src/main/java/i/am/shiro/amai/util`: extension helpers for navigation, fragment arguments, SavedStateHandle delegates, ViewBinding inflation, Room/API mapping, and dialog display.
- `app/src/main/res`: XML layouts, menus, colors, theme, icons, and strings.

## Runtime Flow

- `MainActivity` hosts a single `FragmentContainerView` from `activity_main.xml`.
- First run opens `InitialSetupFragment`, which lists `context.getExternalFilesDirs(null)` choices and stores the selected path in `AmaiPreferences.storagePath`.
- Normal launch opens `HomeFragment`.
- `HomeFragment` owns child `FavoritesFragment` and `NhentaiFragment`, attaching/detaching them behind a Material bottom navigation.
- `NhentaiFragment` is a Compose-backed browse/search screen. It observes `NhentaiViewModel.booksLive` and `isLoadingLive`, supports refresh/sort/search, and triggers pagination when items bind near the end.
- `FavoritesFragment` is XML/ViewBinding-backed. It shows locally favorited books, supports search/sort, opens details, and long-presses into `DeleteBookDialog`.
- `SearchFragment` submits queries through the activity-scoped `MainViewModel`.
- `DetailFragment` is Compose-backed. It loads detail state from Room, refreshes detail from the API, toggles favorites, shares URLs, opens the reader, and sends tag-click searches through `MainViewModel`.
- `ReadFragment` is XML/ViewBinding-backed. It displays page images in `PageRecyclerView`, entering fullscreen while attached.
- Navigation is manually implemented in `util/Navigation.kt` with fragment transactions. This app does not use Jetpack Navigation.

## Data Model And Storage

- Room database class: `AmaiDatabase`.
- Database name: `amai`.
- Schema version and history: Verify `version` in `AmaiDatabase.kt`. Builder configuration lives in `Modules.kt`.
- Entities:
  - `BookEntity`: parent table keyed by `bookId`.
  - `TagEntity`: composite key `bookId`, `type`, `name`; cascades when a book is deleted.
  - `ImageEntity`: composite key `bookId`, `pageIndex`; cascades when a book is deleted.
  - `FavoriteEntity`: keyed by `bookId`, stores `favoriteDate`; foreign key uses `NO_ACTION`.
  - `CachedEntity`: auto id plus unique `bookId`; stores current browse/search result ordering.
- Views:
  - `FavoritesPreviewView`: joins favorites to books for favorites-list display.
  - `CachedPreviewView`: joins cached rows to books and favorites for browse display and favorite badge state.
- Important DAOs:
  - `BookDao.deleteOrphan()` deletes books not referenced by favorites or cache.
  - `FavoritesPreviewDao.findSorted()` switches between newest and oldest favorite sort.
  - `DetailDao.getDetail()` returns `Observable<DetailIntermediate>`.
  - `ImageDao.findByBookId()` returns reader pages ordered by `pageIndex`.
- API-to-entity mapping lives in `util/NhentaiX.kt`.
- File deletion for favorited items happens in `DeleteBookDialog` at `File(preferences.storagePath!!).resolve(bookId.toString()).deleteRecursively()`.

## Network

- Server API documentation https://nhentai.net/api/v2/openapi.json
- Human-readable https://nhentai.net/api/v2/docs
- Debug builds enable OkHttp body logging through `HttpLoggingInterceptor`; release builds set logging to `NONE`.

## UI Conventions

- XML-backed screens use ViewBinding generated from resource ids.
- Compose-backed screens are embedded from fragments using `ComposeView` and `DisposeOnViewTreeLifecycleDestroyed`.
- Compose theme is `AmaiTheme`; verify current color schemes in `compose/AmaiTheme.kt`.
- XML theme is `Theme.Material3.Dark.NoActionBar`; verify in `res/values/themes.xml`.
- Browse and detail Compose layouts use adaptive grids with 150 dp cells and Coil `AsyncImage`.
- Legacy favorites list uses `RecyclerView` with `StaggeredGridLayoutManager`; verify span counts in `res/values/integers.xml`.
- Use existing string resources for visible text. Add strings to `res/values/strings.xml` when introducing new UI text.
- Use existing icons/drawables where possible before adding assets.

## ViewModel And Reactive Patterns

- `ViewModelFactory` manually constructs ViewModels using Koin plus `SavedStateHandle`. Verify supported ViewModels in `viewmodel/factory/ViewModelFactory.kt`.
- `Fragment.amaiViewModels<T>()` in `FragmentX.kt` wires fragments to that factory.
- `SavedStateHandle` delegates live in `SavedStateHandleX.kt`; fragment argument delegates live in `FragmentX.kt`.
- Existing ViewModels use RxJava `Disposable` fields and explicit disposal in `onCleared()`.
- Main shared search events use `MainViewModel.searchEventLive` with a mutable `SearchEvent` carrying per-consumer flags.
- `NhentaiViewModel` caches remote browse/search results in Room's `CachedEntity` table, then exposes `CachedPreviewView` rows from Room.
- `DetailViewModel` exposes Room detail state as an Rx `Observable<DetailModel>` and separately refreshes remote detail with `nhentaiApi.getOne(bookId).retry()`.

## Known Code Quirks

These were observed during research. Re-check the named files before acting on them:

- `HomeFragment.kt` set bottom navigation selection to `navigation_nhentai` in both favorites and nhentai branches; inspect carefully before relying on that logic.
- `FavoritesPreviewAdapter.kt` had `DiffCallback.areContentsTheSame()` always returning `true`; content changes may not rebind existing rows.
- `fragment/dialog/NhentaiSortDialog.kt` was marked `@Deprecated("remove this")` but was still used by `NhentaiFragment.kt`.
- `DetailViewModel.kt` used unbounded `retry()` in `loadRemote()`.
- Several call sites used `!!`; search with `rg -n "!!" app/src/main/java` before changing intent parsing, preferences, or LiveData assumptions.
- `ReadFragment.kt` and `PageRecyclerView.java` handled fullscreen, page snapping, tap zones, and volume-key page flips. Changes here should be manually tested on a device/emulator.
- `PageRecyclerView.java` had a TODO noting volume key handling should move to the fragment root view.
- No download implementation was visible in current source even though some download strings remained; verify with `rg -n "download|Download" app/src/main`.

## Editing Guidelines

- **Respect user work**: Do not overwrite unsaved changes. Always run `git -c safe.directory=C:/android_projects/Amai status --short` before editing.
- **Handle stale content errors**: If an edit fails because the file was modified since it was last read, re-read the file immediately to incorporate the latest changes before retrying.
- **Preserve Architecture**: Do not refactor architecture (e.g., hybrid to pure Compose) or remove resources unless explicitly requested.
- **Database integrity**: If changing Room entities, DAOs, or views:
  - Update `AmaiDatabase.version` if the schema changes.
  - Run a build so KSP/Room exports a new schema JSON.
  - Decide whether destructive migration is acceptable or if explicit migrations are needed.

## Verification

- Build and Lint the app module after making changes
- **Lint Reports**: Inspect `app/build/reports/lint-results-debug.html` or `.txt` for project health.
- **Testing State**: No unit or instrumentation tests were present at research time.
- **UI Verification**: Use Compose previews where available.
- **Manual Flow**: Ask the user to manually exercise affected runtime flows (e.g., database persistence, network loading, or reader interaction) to ensure no regressions.

## Common Search Patterns

Use these patterns to quickly discover project state or identify areas for modification.

- **Fragile or Legacy Code**: `TODO`, `FIXME`, `@Deprecated`, `!!`, `retry\(`, `fallbackToDestructiveMigration`
- **UI Entry Points**: `class .*Fragment`, `ComposeView`, `Fragment\(R.layout`
- **Database Schema**: `@Database`, `@Entity`, `@Dao`, `@DatabaseView`
- **Network Constants & API**: `baseUrl`, `@GET`, `User-Agent`, `API_BASE_URL`, `IMAGE_BASE_URL`
- **Reactive Patterns**: `MutableLiveData`, `Observable`, `Single`, `Completable`, `Disposable`
- **UI Resource Map**: Use `find_files` with patterns like `layout/*.xml`, `values/*.xml`, or `compose/*.kt`
