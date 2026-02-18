# App Theming

## 1. Overview
The application supports three distinct theme modes:
1.  **Light**: Standard light theme.
2.  **Dark**: Standard dark theme (Material Dark Grey `#212121`/`#141218`).
3.  **Pure Black (AMOLED)**: A variant of the dark theme using `#000000` for backgrounds to save battery on OLED screens.

The theming system is a hybrid of:
*   **Android Views (XML)**: Controlled via `styles.xml` and `setTheme()`.
*   **Jetpack Compose**: Controlled via `MaterialTheme` and manual `darkColorScheme` configuration.
*   **Core Logic**: A platform-agnostic `Theme` class hierarchy (`Themes.kt`) used by shared logic.

## 2. Source of Truth

The user's theme preference is persisted in `SharedPreferences` (wrapped by the `Preferences` class).

| Key | Type | Values | Description |
| :--- | :--- | :--- | :--- |
| `pref_theme` | Int | `0` (Automatic), `1` (Dark), `2` (Light) | Determines the base mode. |
| `pref_pure_black` | Boolean | `true` / `false` | Modifies Dark mode to use pure black. |

**File:** `uhabits-core/src/jvmMain/java/org/isoron/uhabits/core/preferences/Preferences.kt`

## 3. Runtime Application Flow

The theme is applied programmatically at runtime, typically in `Activity.onCreate()`.

### Initialization
The central controller is `AndroidThemeSwitcher`.
**File:** `uhabits-android/src/main/java/org/isoron/uhabits/activities/AndroidThemeSwitcher.kt`

1.  **Determination**: `AndroidThemeSwitcher` checks `pref_theme` and `pref_pure_black`.
    *   It handles "Automatic" by checking `context.resources.configuration.uiMode` for `UI_MODE_NIGHT_YES`.
2.  **Application**:
    *   Calls `context.setTheme(resId)` with the appropriate XML style.
    *   Sets `window.navigationBarColor` manually.
    *   Updates its `currentTheme` property with a `Theme` object (Light/Dark/PureBlack) for shared code usage.

### Theme Modes & XML Styles
Defined in: `uhabits-android/src/main/res/values/styles.xml`

| Mode | XML Style | Parent | Notes |
| :--- | :--- | :--- | :--- |
| **Light** | `AppBaseTheme` | `Theme.App.M3` | Inherits Material 3 Light. |
| **Dark** | `AppBaseThemeDark` | `ThemeOverlay.MaterialComponents.Dark.ActionBar` | **Legacy/Mixed**. Overrides specific colors to be dark. Uses `@color/md_theme_dark_surface` (`#141218`) for surface. |
| **Pure Black** | `AppBaseThemeDark.PureBlack` | `AppBaseThemeDark` | Overrides backgrounds (`windowBackgroundColor`, `colorSurface`, `cardBgColor`) to `@color/black`. |

**Note:** `AppBaseTheme` inherits from a proper Material 3 theme (`Theme.App.M3`), but `AppBaseThemeDark` inherits from a Material Components Overlay. This inconsistency is patched by manually defining Material 3 attributes (like `colorSurface`) in `AppBaseThemeDark`.

## 4. Resource Mapping

### Colors
*   **Palettes**: `uhabits-android/src/main/res/values/colors.xml` (contains `lightPalette`, `darkPalette` arrays).
*   **Material 3 Colors**: `uhabits-android/src/main/res/values/m3_colors.xml`.
*   **Dark Overrides**: `uhabits-android/src/main/res/values-night/m3_colors.xml`.

**Important**: Because the app applies themes manually via `setTheme` (and does not force `AppCompatDelegate` night mode), resource qualifiers like `-night` **do not always work** if the system is Light but the app is Dark. To accept this, fixed dark colors (e.g., `md_theme_dark_surface`) are explicitly used in `AppBaseThemeDark`.

### Attributes
Common attributes used in layouts:
*   `?attr/colorSurface`: Background for screens/surfaces.
*   `?attr/cardBgColor`: Background for cards (Grey vs Black).
*   `?attr/contrast0`...`contrast100`: Custom attributes for text/icon contrast levels.

## 5. Component-Specific Details

### Views (XML)
*   **Fragments/Activities**: Apply background color manually or via `android:background="?attr/colorSurface"`.
    *   *Example:* `SettingsFragment.kt` manually sets background using `StyledResources`.
*   **StyledResources**: Helper class to resolve attributes from the *current* context theme.
    *   **File:** `uhabits-android/src/main/java/org/isoron/uhabits/utils/StyledResources.kt`

### Jetpack Compose
*   **Usage**: Explicitly configured in Activities (e.g., `SettingsActivity.kt`).
*   **Theming**: Since there is no global `AppTheme` composable, `MaterialTheme` is often instantiated manually.
    *   *Code:* `MaterialTheme(colorScheme = if (isDark) darkColorScheme() else lightColorScheme())`.
    *   **Warning**: This relies on Compose's default color values unless `darkColorScheme(...)` parameters are explicitly overridden to match XML colors.

### Pure Black Implementation
Pure Black is implemented as:
1.  **XML**: A separate style `AppBaseThemeDark.PureBlack` that overrides generic background attributes to `#000000`.
2.  **Core**: A `PureBlackTheme` class in `Themes.kt` that returns `Color(0x000000)` for background properties.
3.  **Runtime**: `AndroidThemeSwitcher` checks `isPureBlackEnabled` and applies the specific style.

## 6. Pitfalls & Known Issues

1.  **Resource Qualifiers (`-night`) Failure**:
    *   If System = Light and App = Dark, `values-night` resources are **NOT** loaded.
    *   *Fix:* Do not rely on `values-night` for manually applied themes. Use explicit color references in `AppBaseThemeDark`.
2.  **Dual Source of Truth**:
    *   Colors are defined in XML (`styles.xml`) AND Kotlin (`Themes.kt`).
    *   *Risk:* Changing a color in XML does not update the `Theme` class used by custom views/core logic. Always update both.
3.  **Theme Parent Inconsistency**:
    *   Light theme uses M3 parent. Dark theme uses MaterialComponents Overlay. This requires manual backfilling of M3 attributes (like `colorSurface`) in the Dark theme.
4.  **Window Background**:
    *   Some root views require manual background setting (e.g., `applyRootViewInsets` helper) because transparent windows or toolbars might reveal the wrong underlying window color if the theme isn't fully applied to the window decor.

## 7. How to Add a New Theme Mode

1.  **Define Style**: Add a new style in `styles.xml` (e.g., `AppBaseThemeDark.Midnight`).
2.  **Define Core Class**: Create a class in `Themes.kt` (e.g., `MidnightTheme : DarkTheme()`).
3.  **Update Switcher**:
    *   Update `ThemeSwitcher.kt` (Core) to include logic for the new mode.
    *   Update `AndroidThemeSwitcher.kt` (Android) to apply the new XML style and instantiate the new Core class.
4.  **Update Preferences**: Add any necessary toggles or logic in `Preferences.kt` and `preferences.xml`.

## 8. Troubleshooting

*   **Issue: View is White in Dark Mode.**
    *   *Check:* Is the View using `?attr/colorSurface` or similar?
    *   *Check:* Does `AppBaseThemeDark` have that attribute defined explicitly? (If it relies on inheritance or `-night` qualifiers, it might be resolving to Light).
*   **Issue: Compose component colors don't match Views.**
    *   *Check:* Are you using default `darkColorScheme()`?
    *   *Fix:* Manually override the colors in the `darkColorScheme(...)` call to match `styles.xml` values (e.g., `surface = Color(0xFF141218)`).
