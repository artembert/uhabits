# Feature: Redesign CheckmarkDialog with Material 3 Expressive and Compose

## Feature Description

Redesign the `CheckmarkDialog` to use Jetpack Compose and Material Design 3 Expressive styles. This dialog is used for checking off habits (Yes, No, Skip, Unknown). The new implementation will replace the legacy View-based `CheckmarkPopupBinding` with a modern, composable-based UI that adheres strictly to Material 3 guidelines, using standard components and iconography instead of replicating the custom legacy design.

## User Story

As a user
I want to interact with a familiar and modern checkmark dialog
So that I can easily mark my habits as done, skipped, or failed using standard, accessible, and beautiful Material 3 components.

## Problem Statement

The current `CheckmarkDialog` uses a custom XML layout (`checkmark_popup.xml`) with FontAwesome icons and non-standard button styling. This legacy design feels out of place in a modern Android app and requires manual maintenance of styles. We need to transition to standard Material 3 Expressive components (like `IconButton`, `OutlinedTextField`, and standard Dialog containers) to ensure consistency, accessibility, and ease of maintenance.

## Solution Statement

We will create a new Jetpack Compose implementation for the content of `CheckmarkDialog`, focusing on standard M3 components.
1.  **Standard Components**: Use `androidx.compose.material3` components (`OutlinedTextField`, `IconButton`, `FilledTonalIconButton`) with their default styles. Do not force custom colors or shapes unless required by the M3 theme itself.
2.  **Standard Iconography**: Replace FontAwesome icons with standard Material Icons (`Icons.Default.Check`, `Icons.Default.Close`, `Icons.Default.SkipNext`, `Icons.Default.QuestionMark`).
3.  **Layout**: Use a standard `Column` layout with appropriate spacing (via M3 tokens) for the notes field and a `Row` (or `FlowRow`) for the action buttons.
4.  **DialogFragment Integration**: Update `CheckmarkDialog` to host this new composable via `ComposeView`.
5.  **Legacy Support**: The existing `checkmark_popup.xml` will be preserved for `NumberDialog` usage only.

## Relevant Files

Use these files to implement the feature:

- `uhabits-android/src/main/java/org/isoron/uhabits/activities/common/dialogs/CheckmarkDialog.kt`: Main entry point, to be updated to use Compose.
- `uhabits-android/src/main/java/org/isoron/uhabits/activities/common/dialogs/CheckmarkDialogContent.kt`: **New File**. The Compose implementation using standard M3 components.
- `uhabits-android/src/main/res/layout/checkmark_popup.xml`: Reference for original layout structure (do not delete, used by `NumberDialog`).
- `uhabits-android/src/main/java/org/isoron/uhabits/activities/common/views/AppTheme.kt`: Ensure the composable is wrapped in the app's theme.

## Relevant research docstring

- [Material Design 3 Dialogs](https://m3.material.io/components/dialogs/overview)
  - Guidelines on dialog anatomy and usage.
- [Material Design 3 Icons](https://fonts.google.com/icons)
  - Standard Material Symbols/Icons.
- [Jetpack Compose Material 3](https://developer.android.com/jetpack/compose/designsystems/material3)
  - Using M3 components in Compose.

## Implementation Plan

### Phase 1: Foundation

Create the `CheckmarkDialogContent` composable using standard M3 components. This will be a pure UI component.

### Phase 2: Core Implementation

Integrate the `CheckmarkDialogContent` into `CheckmarkDialog` using `ComposeView`.

### Phase 3: Integration

Verify the dialog appears correctly in the app and that the standard M3 styling integrates well with the rest of the application (even if the rest is mixed View/Compose).

## Step by Step Tasks

### 1. Create CheckmarkDialogContent Composable

- Create `uhabits-android/src/main/java/org/isoron/uhabits/activities/common/dialogs/CheckmarkDialogContent.kt`.
- Implement `CheckmarkDialogContent` taking `notes` and callbacks.
- Use `Surface` or `Column` with standard M3 background and padding.
- Add `OutlinedTextField` for notes with standard label ("Notes").
- Add a `Row` for actions:
    - **Yes**: `FilledTonalIconButton` (or `Button` if space permits) with `Icons.Default.Check`. Use `ButtonDefaults.filledTonalButtonColors()` or rely on theme primary color.
    - **No**: `IconButton` (or `OutlinedButton`) with `Icons.Default.Close`.
    - **Skip**: `IconButton` (or `OutlinedButton`) with `Icons.Default.SkipNext` (or similar).
    - **Unknown**: `IconButton` (or `OutlinedButton`) with `Icons.Default.QuestionMark`.
- Ensure buttons use standard `onClick` ripples and states.
- **Do not** apply custom `ColorStateList` or manual typeface setting (FontAwesome). rely on `Icon` composable.

### 2. Update CheckmarkDialog to use Compose

- Modify `uhabits-android/src/main/java/org/isoron/uhabits/activities/common/dialogs/CheckmarkDialog.kt`.
- In `onCreateView`, return a `ComposeView`.
- Set content to `AppTheme { CheckmarkDialogContent(...) }`.
- Pass necessary state (notes, current value) and callbacks.
- Ensure the dialog itself (the `DialogFragment` window) is clean (transparent background if the composable handles the surface, or standard dialog background). *Recommendation: Let the Composable provide the Surface/Card if using `Dialog` composable, but here we are in a `DialogFragment`, so the window background might need to be transparent so the Composable Surface shows.*

### 3. Polish and Verify

- Verify focus handling on the text field.
- Verify that the standard icons convey the correct meaning (Check = Done, Close = Fail, etc.).
- Ensure dark mode support works automatically via M3 components.

## Testing Strategy

### Unit Tests

- `uhabits-android/src/androidTest/java/org/isoron/uhabits/activities/common/dialogs/CheckmarkDialogContentTest.kt`:
    - Verify all 4 buttons exist and have correct content descriptions.
    - Verify text field input updates state.
    - Verify clicking buttons invokes `onAction`.

### Integration Tests

- Manual verification of the dialog opening, interaction, and result saving.

### Edge Cases

- IME (Keyboard) handling: Ensure keyboard doesn't cover buttons (standard Dialog behavior usually handles this, but verify).
- Accessibility: Ensure all standard components have default semantics (which they do) and add meaningful content descriptions for the icons (e.g., "Mark as Done", "Mark as Failed").

## Acceptance Criteria

- `CheckmarkDialog` uses standard Material 3 Expressive components (`IconButton`, `OutlinedTextField`).
- No FontAwesome icons are used in the new dialog.
- No custom "button styles" from XML are used; only Compose M3 defaults.
- Functionality is preserved.
