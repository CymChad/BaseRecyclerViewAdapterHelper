# Repository Guidelines

## Project Structure & Module Organization
This is an Android Gradle project with Kotlin DSL build files. `settings.gradle` includes two modules:

- `library/`: the reusable RecyclerView adapter library published as `io.github.cymchad:BaseRecyclerViewAdapterHelper4`.
- `app/`: the sample Android application demonstrating library usage.

Main source lives under `app/src/main/java` and `library/src/main/java`. Android resources are under each module's `src/main/res`. Keep public library APIs in the `com.chad.library.adapter4` namespace and sample-only code in `com.chad.baserecyclerviewadapterhelper`.

## Build, Test, and Development Commands
Use the Gradle wrapper from the repository root:

- `./gradlew assembleDebug`: builds debug artifacts for all modules.
- `./gradlew :app:assembleDebug`: builds the sample app.
- `./gradlew :library:assembleRelease`: builds the release AAR for the library.
- `./gradlew clean`: removes generated build output.
- `./gradlew test`: runs JVM unit tests when test sources exist.
- `./gradlew connectedAndroidTest`: runs instrumentation tests on a connected device or emulator when present.

The project targets Java 17 via Gradle toolchains. Dependencies and plugin versions are centralized in `gradle/libs.versions.toml`.

## Coding Style & Naming Conventions
Use Kotlin for new code unless working in an existing Java file. Follow Android/Kotlin conventions: 4-space indentation, `UpperCamelCase` classes, `lowerCamelCase` functions and properties, and package names matching module namespaces. Prefer clear adapter, view holder, and load-state names that match existing patterns such as `BaseQuickAdapter`, `QuickViewHolder`, and `DefaultTrailingLoadStateAdapter`.

Keep resources lowercase with underscores, for example `brvah_trailing_load_more.xml`. Avoid unrelated formatting churn in mixed Java/Kotlin files.

## Testing Guidelines
Library JVM tests are under `library/src/test` and use Robolectric. Add unit tests under `module/src/test` and Android tests under `module/src/androidTest` when changing behavior. Name tests after the unit under test and expected behavior, for example `BaseDifferAdapterTest`. Run `./gradlew test` before submitting logic changes, and use `connectedAndroidTest` for RecyclerView or UI behavior that needs Android runtime coverage.

## Commit & Pull Request Guidelines
Recent history uses Conventional Commit-style messages, often scoped, such as `fix(adapter): ...`, `refactor(loadState): ...`, and `docs(README): ...`. Keep commits focused and use scopes that match affected areas.

Pull requests should include a concise description, linked issues when applicable, test results, and screenshots or recordings for sample app UI changes. For library API changes, document migration notes and update README or wiki references when needed.

## Security & Configuration Tips
Do not commit `local.properties`, signing keys, Sonatype credentials, or generated publishing output. Publishing credentials are loaded from local properties or environment-specific configuration.
