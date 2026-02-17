# Feature: Floating Toolbar Navigation

## Feature Description

Implement a `FloatingToolbarNavigation` component using Jetpack Compose to handle navigation between the main screens of the application. This component will replace the existing placeholder `HabitsFloatingToolbar` and will be integrated into both the `ListHabitsActivity` (via `ListHabitsRootView`) and `SettingsActivity`.

## User Story

As a user
I want to have a persistent and consistent navigation toolbar
So that I can easily switch between the habit list, settings, and create new habits from anywhere.

## Problem Statement

The current navigation is fragmented or relies on older View-based menus. The `HabitsFloatingToolbar` is a placeholder. We need a unified, modern, Compose-based navigation component that reflects the current screen state and provides quick access to core actions.

## Solution Statement

1.  **Create `FloatingToolbarNavigation` Component**:
    *   Refactor or replace `HabitsFloatingToolbar` with `FloatingToolbarNavigation`.
    *   Use `FilledTonalButton` for the active screen button and `TextButton` for inactive ones.
    *   Include a `FloatingActionButton` for adding habits.
    *   Accept an `activeScreen` parameter of type `Class<*>` (passing the Activity class) to determine the active state using existing entities.
    *   Expose callbacks for navigation actions: `onListClick`, `onAddClick`, `onSettingsClick`.

2.  **Integrate into `ListHabitsActivity`**:
    *   Update `ListHabitsRootView` to use `FloatingToolbarNavigation`.
    *   Expose user interactions (clicks) from `ListHabitsRootView` to `ListHabitsScreen` (the glue between View and Behavior).
    *   Wire these interactions to `ListHabitsBehavior` or directly to `IntentFactory` where appropriate, maintaining the MVVM/MVP hybrid pattern.

3.  **Integrate into `SettingsActivity`**:
    *   Add a `ComposeView` to the `SettingsActivity` layout or root view.
    *   Render `FloatingToolbarNavigation` with `activeScreen = SettingsActivity::class.java`.
    *   Implement actions to return to `ListHabitsActivity` (via `finish()`) and open `HabitTypeDialog` (using `supportFragmentManager`).

## Relevant Files

-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/views/HabitsFloatingToolbar.kt`: Existing placeholder to be refactored/renamed.
-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/ListHabitsRootView.kt`: Host for the toolbar in the list screen.
-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/settings/SettingsActivity.kt`: Host for the toolbar in the settings screen.
-   `uhabits-android/src/main/res/layout/settings_activity.xml`: Layout file for SettingsActivity (to add ComposeView).
-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/edit/HabitTypeDialog.kt`: The dialog to be opened by the "Add" button.
-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/ListHabitsScreen.kt`: Handles navigation logic for the list screen.
-   `uhabits-android/src/main/java/org/isoron/uhabits/intents/IntentFactory.kt`: Central handling for navigation Intents.
-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/ListHabitsActivity.kt`: Reference for the list activity class.

### New Files

-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/common/views/FloatingToolbarNavigation.kt`: The new Compose component (renamed from `HabitsFloatingToolbar.kt`).

## Relevant research docstring

-   [Jetpack Compose Interop](https://developer.android.com/jetpack/compose/interop/interop-apis)
    -   `ComposeView`: Used to embed Compose content in XML layouts.
-   [Material3 Navigation Bar](https://m3.material.io/components/navigation-bar/overview)
    -   Although strictly not a standard `NavigationBar`, the design mimics a floating bottom bar pattern.
-   `docs/routing-and-navigation.md`: Project-specific routing documentation.

## Implementation Plan

### Phase 1: Foundation
-   Rename `HabitsFloatingToolbar.kt` to `FloatingToolbarNavigation.kt`.
-   Implement the `FloatingToolbarNavigation` Composable with the required UI logic (highlighting based on `activeScreen`).

### Phase 2: Core Implementation
-   Update `ListHabitsRootView.kt` to use `FloatingToolbarNavigation`.
-   Define callbacks in `ListHabitsRootView` (`onSettingsClick`, `onAddClick`, etc.).
-   Update `ListHabitsScreen.kt` to listen to these callbacks and trigger the appropriate `Behavior` method or `IntentFactory` call.

### Phase 3: Integration
-   Add `ComposeView` to `settings_activity.xml`.
-   Update `SettingsActivity.kt` to render `FloatingToolbarNavigation`.
-   Wire up navigation logic in `SettingsActivity` using `finish()` for "List" and `HabitTypeDialog` for "Add".

## Step by Step Tasks

### 1. Create FloatingToolbarNavigation Component
-   Rename `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/views/HabitsFloatingToolbar.kt` to `uhabits-android/src/main/java/org/isoron/uhabits/activities/common/views/FloatingToolbarNavigation.kt`.
-   Move it to `common/views` package.
-   Update the Composable `FloatingToolbarNavigation` to accept:
    -   `activeScreen: Class<*>`
    -   `onListClick: () -> Unit`
    -   `onAddClick: () -> Unit`
    -   `onSettingsClick: () -> Unit`
-   Implement the UI logic:
    -   If `activeScreen == ListHabitsActivity::class.java`: "List" button is `FilledTonalButton`, "Settings" is `TextButton`.
    -   If `activeScreen == SettingsActivity::class.java`: "List" button is `TextButton`, "Settings" is `FilledTonalButton`.
    -   "Add" button is always `FloatingActionButton`.

### 2. Integrate into ListHabitsActivity
-   Modify `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/ListHabitsRootView.kt`:
    -   Update imports.
    -   Replace `HabitsFloatingToolbar` with `FloatingToolbarNavigation`.
    -   Pass `activeScreen = ListHabitsActivity::class.java`.
    -   Expose callbacks: `var onSettingsClicked: () -> Unit = {}`, `var onAddClicked: () -> Unit = {}`.
    -   Invoke these callbacks from the Composable's actions.
- Modify `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/ListHabitsScreen.kt`:
    - In `onAttached()`, set `rootView.get().onSettingsClicked = { showSettingsScreen() }`.
    - Set `rootView.get().onAddClicked = { showSelectHabitTypeDialog() }`.

### 3. Integrate into SettingsActivity
-   Modify `uhabits-android/src/main/res/layout/settings_activity.xml`:
    -   Add `androidx.compose.ui.platform.ComposeView` inside the `RelativeLayout`.
    -   Align it to the bottom center.
-   Modify `uhabits-android/src/main/java/org/isoron/uhabits/activities/settings/SettingsActivity.kt`:
    -   Find the `ComposeView`.
    -   Set content to `FloatingToolbarNavigation`.
    -   Pass `activeScreen = SettingsActivity::class.java`.
    -   Implement `onListClick`: `finish()`.
    -   Implement `onAddClick`: Instantiate `HabitTypeDialog` and show using `supportFragmentManager`.
    -   Implement `onSettingsClick`: Do nothing.

### 4. Verification
-   Lint and Format.
-   Build and run tests.
-   Verify navigation intents match `IntentFactory` patterns where applicable.

## Validation Commands

-   `./gradlew uhabits-android:lintDebug`
-   `./gradlew uhabits-android:testDebugUnitTest`
-   Manual verification:
    -   Launch app -> Toolbar visible, "List" selected.
    -   Click "Add" -> Dialog opens.
    -   Click "Settings" -> Settings Activity opens, Toolbar visible, "Settings" selected.
    -   Click "List" (from Settings) -> Returns to List (activity finished).
    -   Click "Add" (from Settings) -> Dialog opens.
