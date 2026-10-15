# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

BRVAH (BaseRecyclerViewAdapterHelper) — a RecyclerView Adapter helper library for Android (current major version: **v4.x**, published as `io.github.cymchad:BaseRecyclerViewAdapterHelper4`). v4 was rewritten to be fully compatible with `ConcatAdapter`, to split functionality into modules, and to support flexible multi-type layouts plus strengthened up/down load-more.

The repository contains **two Gradle modules**:
- `:library` — the published library (namespace `com.chad.library.adapter4`). This is the artifact.
- `:app` — the demo application that exercises every library feature (`com.chad.baserecyclerviewadapterhelper`). `demo/` holds a prebuilt APK only.

## Build & Toolchain

Gradle with Kotlin DSL (`settings.gradle`, `build.gradle.kts`, `gradle/libs.versions.toml`). Toolchain and SDK versions are pinned per module:

- **JDK 17** toolchain for both modules (CI uses temurin 17).
- `:library` — `compileSdk = 35`, `minSdk = 19`, depends only on `androidx.annotation`, `androidx.recyclerview`, and `compileOnly` `databinding-runtime`. No AndroidX app deps — it is a pure library.
- `:app` — `compileSdk/targetSdk = 36`, `minSdk = 23`, uses viewBinding + dataBinding, Moshi (KSP codegen). All third-party demo deps live here, never in `:library`.

Common commands (run from repo root):

```bash
./gradlew :library:compileDebugKotlin        # what CI runs — fastest library sanity check
./gradlew :library:assembleRelease           # build the AAR
./gradlew :app:assembleDebug                 # build the demo APK
./gradlew clean
```

Library JVM tests use Robolectric and run with `./gradlew :library:testDebugUnitTest`. There are no instrumented tests. Compile affected modules and use the demo app for changes that require device/UI verification.

### Publishing / versioning
- Library version lives in `library/build.gradle.kts` as `val versionName` (currently `4.4.1`). The `:app` `versionName` (`4.3.2`) is independent — update them separately.
- Publishing is configured with `maven-publish` + `signing`. Credentials/signing keys are read from `local.properties` (`signing.keyId`, `signing.password`, `signing.secretKeyRingFile`, `ossrhUsername`, `ossrhPassword`). The active publish repository is the local `$rootDir/Repo` folder (Maven Central upload block is commented out).
- ProGuard consumer rules ship via `consumerProguardFiles("proguard-rules.pro")` in `:library`, so consumers auto-import them; the file itself is mostly a placeholder.

## Library architecture

Everything is under `com.chad.library.adapter4`. The design centers on one abstract base plus adapter composition via `ConcatAdapter`.

### `BaseQuickAdapter<T, VH>` (the base class)
`BaseQuickAdapter.kt` is the foundation for all adapters. Two things define it:

1. **Two data modes**, decided at construction by whether a `DiffUtil.ItemCallback<T>`/`AsyncDifferConfig<T>` is supplied:
   - *Diff mode* — backs `items` with `AsyncListDiffer` (async diffing, no jank). Mutating ops (`submitList`, `add`, `set`, `removeAt`, `swap`, `move`, …) rebuild a mutable list and re-submit.
   - *Plain mode* (`mDiffer == null`) — backs `items` with a plain `List<T>` and calls the corresponding `notifyItem*` directly.
   Every mutating method branches on `mDiffer == null`; when touching one, update **both** branches.

2. **Sealed RecyclerView.Adapter overrides.** `getItemCount()`, `getItemViewType()`, `onCreateViewHolder()`, and `onBindViewHolder()` are `final`. Subclasses implement the **protected** variants instead (`onCreateViewHolder(context, parent, viewType)`, `onBindViewHolder(holder, position, item[, payloads])`, `getItemCount(items)`, `getItemViewType(position, list)`). Do not try to override the final ones.

Other base-class responsibilities: optional **state/empty view** (`isStateViewEnable` + `stateView`, shown when `items` is empty via `StateLayoutVH`), item **animations** (`animationEnable` + `itemAnimation`/`setItemAnimation(AnimationType)`), and click listeners (item / item-child by view id, stored in a `SparseArray`). `items` setter is `@Deprecated` at **ERROR level** — use `submitList()` to replace data.

### Adapter variants (all extend `BaseQuickAdapter`)
- **`BaseMultiItemAdapter<T>`** — multi view-type layouts. Register each type with `addItemType(viewType, OnMultiItemAdapterListener)` and decide types via `onItemViewType { position, list -> }`. The listener (or `OnMultiItem` subclass, which grants `adapter`/`context`) provides `onCreate`/`onBind` per type.
- **`BaseNodeAdapter`** — tree/expandable lists. Subclasses implement `getChildNodeList()` and `isInitialOpen()`; `open()`/`close()`/`openOrClose()`/`closeAll()` drive expansion. Open/closed state is tracked by a custom `NodeSet` matched through `isSameNode()` (override for value-based nodes; default is `===`). See the bilingual KDoc on `isSameNode` — it must uniquely identify a node or expand/collapse state leaks across nodes.
- **`BaseSingleItemAdapter<T, VH>`** — exactly one item (headers/footers). All list mutation methods throw; use `item`/`setItem()` only.
- **`BaseDifferAdapter`** — deprecated; `BaseQuickAdapter` now subsumes it.

### Composition: `QuickAdapterHelper`
`QuickAdapterHelper` wraps a content adapter in a `ConcatAdapter` and exposes `helper.adapter` to set on the `RecyclerView`. Layout order: `LeadingLoadStateAdapter` → before-adapters → **content adapter** → after-adapters → `TrailingLoadStateAdapter`. Built via `QuickAdapterHelper.Builder(contentAdapter)` with optional leading/trailing load-more, then `.build()` or `.attachTo(recyclerView)`. This is the canonical way to get header/footer and up/down load-more in v4 — header/footer are separate small adapters prepended/appended, not built-in fields of the base adapter.

### Load more (`loadState/`)
- `LoadState` — sealed type: `None`, `NotLoading(endOfPaginationReached)`, `Loading`, `Error`.
- `LoadStateAdapter<VH>` — base for a load-more adapter; toggles its single item on/off via `displayLoadStateAsItem()`.
- `TrailingLoadStateAdapter` (tail) / `LeadingLoadStateAdapter` (head), each with a `Default…` implementation. Trailing supports `isAutoLoadMore`, `preloadSize`, and `checkDisableLoadMoreIfNotFullPage()`.

### Supporting utilities
- `viewholder/QuickViewHolder` — convenience `ViewHolder` with cached `findViewById` and chained setters (`setText`, `setVisible`, …).
- `layoutmanager/QuickGridLayoutManager` — `GridLayoutManager` that grants full span to adapters implementing `FullSpanAdapterType`, and to `BaseQuickAdapter` view types for which `isFullSpanItem(type)` is true (the empty/state view is full-span by default). Use it (or your own `SpanSizeLookup`) when an item must span all columns.
- `dragswipe/QuickDragAndSwipe` + `DragSwipeExt.kt` — drag-and-swipe on top of `ItemTouchHelper`.
- `animation/*` — `ItemAnimator` implementations (`AlphaIn`, `ScaleIn`, `SlideIn{Left,Right,Bottom}`).
- `util/AdapterUtils.kt` — `ViewGroup.getItemView(layoutResId)` and `ViewHolder.asStaggeredGridFullSpan()`.

### Internal resource IDs
The library declares IDs in `library/src/main/res/values/ids.xml` (e.g. `BaseQuickAdapter_empty_view`, `BaseQuickAdapter_key_multi`) used for internal view tagging and the empty-view `viewType`. The `EMPTY_VIEW` companion constant points at `R.id.BaseQuickAdapter_empty_view`.

## Conventions

- **Bilingual KDoc.** Public API documentation is written in **both Chinese and English** (Chinese typically first, then English). Match this style when adding or editing public APIs.
- **Builder/`apply` chaining.** Setters in `BaseQuickAdapter` and `QuickAdapterHelper.Builder` return `apply`/the builder for fluent config.
- Keep `:library` dependency-free of UI libs — it should only depend on AndroidX `annotation`/`recyclerview` (plus `compileOnly` databinding). Put demo-only dependencies in `:app`.
