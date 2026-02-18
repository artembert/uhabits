# Loop Habit Tracker Project Overview

## 1. Project Purpose
Loop Habit Tracker is a privacy-focused, open-source mobile application designed to help users create and maintain good habits. It features flexible schedules, detailed charts/statistics, reminders, and widgets, all while operating completely offline.

## 2. Architecture
*   **Modular Monolith:** Split into `uhabits-android` (UI/Platform) and `uhabits-core` (Domain Logic).
*   **Pattern:** MVVM/MVP hybrid. The core module uses "Presenters" (e.g., `ShowHabitPresenter`) and "Behaviors" to manage UI logic, while the Android module handles the View implementation.
*   **Dependency Injection:** Dagger 2 is used for dependency injection across the app (`DaggerHabitsApplicationComponent`).
*   **Data Persistence:** Custom SQLite implementation (via `AndroidDatabaseOpener` and migration scripts) rather than an ORM like Room.

## 3. Core Principles
*   **Privacy First:** No internet permission required; all data is local.
*   **Open Source:** GPLv3 licensed.
*   **Zero Ads/Tracking:** Explicit commitment to no advertisements or third-party tracking.
*   **Performance:** Optimized for speed and lightweight footprint.

## 4. Tech Stack
*   **Language:** Kotlin (primary), with some Java legacy code.
*   **UI:** Hybrid approach. Existing views use **XML Layouts** with ViewBinding. The project is currently migrating to **Jetpack Compose** (dependencies present, `buildFeatures.compose = true`).
*   **Async:** Kotlin Coroutines.
*   **Build System:** Gradle (Kotlin DSL).
*   **Testing:** JUnit 4/5, Mockito, AndroidX Test.

## 5. Key Requirements
*   **SDK Levels:** Min SDK 28, Target SDK 36.
*   **Code Quality:** Enforced via KtLint.
*   **Conventions:**
    *   Core logic must remain in `uhabits-core` to facilitate potential multiplatform expansion (though currently targeting JVM).
    *   Database migrations are handled via raw SQL files in `uhabits-core/src/jvmMain/resources/migrations`.

## 6. Current State
*   The application is stable and fully featured.
*   **Migration Status:** The codebase is in a transitional state between XML/View-based UI and Jetpack Compose. New UI features should likely prefer Compose.
*   **Module Structure:**
    *   `uhabits-core`: Contains Models (`Habit`, `Entry`), Business Logic, and Database interactions.
    *   `uhabits-android`: Contains Activities, Fragments, Receivers, and Android-specific implementations.
