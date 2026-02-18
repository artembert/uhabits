/*
 * Copyright (C) 2016-2025 Álinson Santos Xavier <git@axavier.org>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.isoron.uhabits.activities.common.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.isoron.uhabits.core.models.Entry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CheckmarkDialogContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysInitialState() {
        composeTestRule.setContent {
            MaterialTheme {
                CheckmarkDialogContent(
                    notes = "Initial Note",
                    habitName = "Running",
                    onNotesChanged = {},
                    onAction = {},
                    onDismissRequest = {},
                    primaryColor = Color.Blue
                )
            }
        }

        // Title and habit name
        composeTestRule.onNodeWithText("Have you completed this habit today?").assertExists()
        composeTestRule.onNodeWithText("Running").assertExists()
        composeTestRule.onNodeWithText("Initial Note").assertExists()
        
        // Buttons (they have text now)
        composeTestRule.onNodeWithText("Check").assertExists()
        composeTestRule.onNodeWithText("Skip").assertExists()
        composeTestRule.onNodeWithText("No").assertExists()
        composeTestRule.onNodeWithText("Question").assertExists()
    }

    @Test
    fun updatesNotes() {
        var capturedNotes = ""
        composeTestRule.setContent {
            MaterialTheme {
                CheckmarkDialogContent(
                    notes = "",
                    habitName = "Running",
                    onNotesChanged = { capturedNotes = it },
                    onAction = {},
                    onDismissRequest = {},
                    primaryColor = Color.Blue
                )
            }
        }

        composeTestRule.onNodeWithText("Notes").performTextInput("New Note")
        assertEquals("New Note", capturedNotes)
    }

    @Test
    fun triggersActions() {
        var capturedAction = -1
        composeTestRule.setContent {
            MaterialTheme {
                CheckmarkDialogContent(
                    notes = "",
                    habitName = "Running",
                    onNotesChanged = {},
                    onAction = { capturedAction = it },
                    onDismissRequest = {},
                    primaryColor = Color.Blue
                )
            }
        }

        composeTestRule.onNodeWithText("Check").performClick()
        assertEquals(Entry.YES_MANUAL, capturedAction)

        composeTestRule.onNodeWithText("Skip").performClick()
        assertEquals(Entry.SKIP, capturedAction)
        
        composeTestRule.onNodeWithText("No").performClick()
        assertEquals(Entry.NO, capturedAction)

        composeTestRule.onNodeWithText("Question").performClick()
        assertEquals(Entry.UNKNOWN, capturedAction)
    }
}
