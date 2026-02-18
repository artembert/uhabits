# Routing and Navigation

In the Loop Habit Tracker application, routing between screens and views is primarily managed through a traditional **Activity-based navigation** system, abstracted by a central **`IntentFactory`** and a **Behavior/Screen pattern**.

## 1. Activity-Based Routing
The application is structured into several distinct Activities for each major feature (e.g., `ListHabitsActivity`, `ShowHabitActivity`, `EditHabitActivity`, and `SettingsActivity`). Navigation between these screens is handled by starting new Activities using standard Android `Intent`s.

## 2. IntentFactory
To decouple navigation logic from UI implementation, the project uses a central **`IntentFactory`** class (located in `uhabits-android/.../intents/`). This class is responsible for creating all `Intent` objects required for navigation, ensuring that deep-linking and parameter passing (like `habitId` or `habitType`) are handled consistently.

```kotlin
// Example from IntentFactory.kt
fun startShowHabitActivity(context: Context, habit: Habit) =
    Intent(context, ShowHabitActivity::class.java).apply {
        data = Uri.parse(habit.uriString)
    }
```

## 3. Behavior and Screen Pattern (MVVM/MVP Hybrid)
The routing logic is often driven by the **`uhabits-core`** module through "Behavior" classes (e.g., `ListHabitsBehavior`).
- **Behavior (Core):** Decides *when* a navigation should occur based on business logic.
- **Screen (Android):** Implements an interface defined in the core (e.g., `ListHabitsBehavior.Screen`) to perform the actual platform-specific navigation.

```kotlin
// Example from ListHabitsScreen.kt (Android implementation)
override fun showHabitScreen(h: Habit) {
    val intent = intentFactory.startShowHabitActivity(activity, h)
    activity.startActivity(intent)
}
```

## 4. Dialogs and Specialized Fragments
While major screens are Activities, modal interactions (like color picking or habit type selection) are implemented as **`DialogFragment`s**. The **Settings** screen also utilizes a **`SettingsFragment`** (`PreferenceFragmentCompat`) hosted within the `SettingsActivity`.

## 5. Jetpack Compose Integration
The project is currently migrating to **Jetpack Compose**. Navigation from Compose-based components is integrated into the existing system using callbacks. For example, a click in a Compose floating toolbar triggers a callback in the hosting Activity, which then uses the `IntentFactory` to navigate.

## Summary of Routing Flow
1. **User Action:** User clicks a habit in the list.
2. **Behavior Logic:** `ListHabitsBehavior` (in `core`) receives the event and calls `screen.showHabitScreen(habit)`.
3. **Screen Implementation:** `ListHabitsScreen` (in `android`) uses `IntentFactory` to create an `Intent`.
4. **Android Navigation:** The Activity starts the `ShowHabitActivity` via `context.startActivity(intent)`.
