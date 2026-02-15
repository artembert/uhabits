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
    *   Accept a `currentScreen` parameter to determine the active state.
    *   Expose callbacks for navigation actions: `onListClick`, `onAddClick`, `onSettingsClick`.

2.  **Integrate into `ListHabitsActivity`**:
    *   Update `ListHabitsRootView` to use `FloatingToolbarNavigation`.
    *   Set `currentScreen` to `LIST`.
    *   Implement actions to open `HabitTypeDialog` and `SettingsActivity`.

3.  **Integrate into `SettingsActivity`**:
    *   Add a `ComposeView` to the `SettingsActivity` layout or root view.
    *   Render `FloatingToolbarNavigation`.
    *   Set `currentScreen` to `SETTINGS`.
    *   Implement actions to return to `ListHabitsActivity` and open `HabitTypeDialog`.

## Relevant Files

-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/views/HabitsFloatingToolbar.kt`: Existing placeholder to be refactored/renamed.
-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/ListHabitsRootView.kt`: Host for the toolbar in the list screen.
-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/settings/SettingsActivity.kt`: Host for the toolbar in the settings screen.
-   `uhabits-android/src/main/res/layout/settings_activity.xml`: Layout file for SettingsActivity (to add ComposeView).
-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/edit/HabitTypeDialog.kt`: The dialog to be opened by the "Add" button.
-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/ListHabitsScreen.kt`: Handles navigation logic for the list screen.

### New Files

-   `uhabits-android/src/main/java/org/isoron/uhabits/activities/common/views/FloatingToolbarNavigation.kt`: The new Compose component (renamed from `HabitsFloatingToolbar.kt`).

## Relevant research docstring

-   [Jetpack Compose Interop](https://developer.android.com/jetpack/compose/interop/interop-apis)
    -   `ComposeView`: Used to embed Compose content in XML layouts.
-   [Material3 Navigation Bar](https://m3.material.io/components/navigation-bar/overview)
    -   Although strictly not a standard `NavigationBar`, the design mimics a floating bottom bar pattern.

## Implementation Plan

### Phase 1: Foundation
-   Rename `HabitsFloatingToolbar.kt` to `FloatingToolbarNavigation.kt`.
-   Define `NavigationScreen` enum or sealed class.
-   Implement the `FloatingToolbarNavigation` Composable with the required UI logic (highlighting based on state).

### Phase 2: Core Implementation
-   Update `ListHabitsRootView.kt` to use `FloatingToolbarNavigation` with correct callbacks.
-   Add `ComposeView` to `settings_activity.xml`.
-   Update `SettingsActivity.kt` to render `FloatingToolbarNavigation`.

### Phase 3: Integration
-   Wire up navigation logic in `ListHabitsRootView` and `SettingsActivity`.
-   Ensure correct behavior for "Add" button in both contexts.
-   Verify back stack behavior (e.g., clicking "List" from Settings should finish Settings).

## Step by Step Tasks

### 1. Create FloatingToolbarNavigation Component
-   Rename `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/views/HabitsFloatingToolbar.kt` to `uhabits-android/src/main/java/org/isoron/uhabits/activities/common/views/FloatingToolbarNavigation.kt`.
-   Move it to `common/views` package if appropriate, or keep in `habits/list/views` if it's specific (but it's used in Settings too, so `common` is better). *Correction: Keep in `list/views` for now to minimize package churn or move to `activities/common/views` if it exists.* I'll check if `activities/common/views` exists. Yes, `uhabits-android/src/main/java/org/isoron/uhabits/activities/common/views` exists.
-   Define `enum class NavigationScreen { LIST_HABITS, SETTINGS }`.
-   Update the Composable `FloatingToolbarNavigation` to accept:
    -   `currentScreen: NavigationScreen`
    -   `onListClick: () -> Unit`
    -   `onAddClick: () -> Unit`
    -   `onSettingsClick: () -> Unit`
-   Implement the UI logic:
    -   If `currentScreen == LIST_HABITS`: "List" button is `FilledTonalButton`, "Settings" is `TextButton`.
    -   If `currentScreen == SETTINGS`: "List" button is `TextButton`, "Settings" is `FilledTonalButton`.
    -   "Add" button is always `FloatingActionButton`.

### 2. Integrate into ListHabitsActivity
-   Modify `uhabits-android/src/main/java/org/isoron/uhabits/activities/habits/list/ListHabitsRootView.kt`:
    -   Update imports.
    -   Replace `HabitsFloatingToolbar` with `FloatingToolbarNavigation`.
    -   Pass `currentScreen = NavigationScreen.LIST_HABITS`.
    -   Implement `onListClick`: Empty or scroll to top.
    -   Implement `onAddClick`: Call `screen.showSelectHabitTypeDialog()` (need to access `ListHabitsScreen` or similar). `ListHabitsRootView` has `HabitCardListViewFactory` etc. It might need a reference to a listener or inject a dependency to handle this.
    -   *Correction*: `ListHabitsRootView` is a View. It might be better to expose a listener or use the existing `ListHabitsMenuBehavior` if possible, but that's for the top menu.
    -   `ListHabitsRootView` seems to be just a view. The `ListHabitsActivity` sets it as content view.
    -   I can add a callback property to `ListHabitsRootView` or handle clicks in the View itself if it has access to the controller.
    -   In `ListHabitsRootView.kt`, there is `screen` injected? No, `screen` is in `ListHabitsActivity`.
    -   Wait, `ListHabitsRootView` is injected into `ListHabitsActivity`.
    -   I can simply use `Context` to cast to `ListHabitsActivity` (unsafe) or better, use an interface.
    -   Actually `ListHabitsRootView` is `@ActivityScope` and `@Inject constructor`.
    -   I can inject `ListHabitsScreen` into `ListHabitsRootView` if it's not circular.
    -   Alternatively, define a listener interface in `ListHabitsRootView` and implement it in `ListHabitsActivity`.

### 3. Integrate into SettingsActivity
-   Modify `uhabits-android/src/main/res/layout/settings_activity.xml`:
    -   Add `androidx.compose.ui.platform.ComposeView` inside the `RelativeLayout`.
    -   Align it to the bottom center.
-   Modify `uhabits-android/src/main/java/org/isoron/uhabits/activities/settings/SettingsActivity.kt`:
    -   Find the `ComposeView`.
    -   Set content to `FloatingToolbarNavigation`.
    -   Pass `currentScreen = NavigationScreen.SETTINGS`.
    -   Implement `onListClick`: `finish()`.
    -   Implement `onAddClick`: Open `HabitTypeDialog`.
    -   Implement `onSettingsClick`: Do nothing.

### 4. Verification
-   Lint and Format.
-   Build and run tests.

## Validation Commands

-   `./gradlew uhabits-android:lintDebug`
-   `./gradlew uhabits-android:testDebugUnitTest`
-   Manual verification:
    -   Launch app -> Toolbar visible, "List" selected.
    -   Click "Add" -> Dialog opens.
    -   Click "Settings" -> Settings Activity opens, Toolbar visible, "Settings" selected.
    -   Click "List" (from Settings) -> Returns to List.
    -   Click "Add" (from Settings) -> Dialog opens.

## Notes

-   Ensure `NavigationScreen` is public and accessible.
-   Be careful with `HabitTypeDialog` dependency in `SettingsActivity`. It might need `supportFragmentManager`. `SettingsActivity` extends `AppCompatActivity`, so it has it.
