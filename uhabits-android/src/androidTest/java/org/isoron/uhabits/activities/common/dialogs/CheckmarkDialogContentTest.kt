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
            CheckmarkDialogContent(
                notes = "Initial Note",
                onNotesChanged = {},
                onAction = {},
                primaryColor = Color.Blue
            )
        }

        composeTestRule.onNodeWithText("Initial Note").assertIsDisplayed()
        // Content descriptions come from string resources. 
        // We assume English locale for tests or check existence by other means if needed.
        // But verifying by content description is best practice.
        // Strings: Check, Skip, No, Question
        composeTestRule.onNodeWithContentDescription("Check").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Skip").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("No").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Question").assertIsDisplayed()
    }

    @Test
    fun updatesNotes() {
        var capturedNotes = ""
        composeTestRule.setContent {
            CheckmarkDialogContent(
                notes = "",
                onNotesChanged = { capturedNotes = it },
                onAction = {},
                primaryColor = Color.Blue
            )
        }

        composeTestRule.onNodeWithText("Notes").performTextInput("New Note")
        assertEquals("New Note", capturedNotes)
    }

    @Test
    fun triggersActions() {
        var capturedAction = -1
        composeTestRule.setContent {
            CheckmarkDialogContent(
                notes = "",
                onNotesChanged = {},
                onAction = { capturedAction = it },
                primaryColor = Color.Blue
            )
        }

        composeTestRule.onNodeWithContentDescription("Check").performClick()
        assertEquals(Entry.YES_MANUAL, capturedAction)

        composeTestRule.onNodeWithContentDescription("Skip").performClick()
        assertEquals(Entry.SKIP, capturedAction)
        
        composeTestRule.onNodeWithContentDescription("No").performClick()
        assertEquals(Entry.NO, capturedAction)

        composeTestRule.onNodeWithContentDescription("Question").performClick()
        assertEquals(Entry.UNKNOWN, capturedAction)
    }
}
