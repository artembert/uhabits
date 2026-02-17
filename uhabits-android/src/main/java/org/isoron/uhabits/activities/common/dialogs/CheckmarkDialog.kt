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

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import org.isoron.uhabits.HabitsApplication
import org.isoron.uhabits.activities.AndroidThemeSwitcher
import org.isoron.uhabits.activities.common.views.AppTheme
import org.isoron.uhabits.core.ui.views.DarkTheme
import org.isoron.uhabits.core.ui.views.LightTheme
import org.isoron.uhabits.core.ui.views.PureBlackTheme

class CheckmarkDialog : AppCompatDialogFragment() {
    var onToggle: (Int, String) -> Unit = { _, _ -> }
    var onDismiss: () -> Unit = {}

    private var dismissedViaSaveAction = false
    private var originalNotes: String = ""
    private var originalValue: Int = 0
    private var currentNotes: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val appComponent = (requireActivity().application as HabitsApplication).component
        val prefs = appComponent.preferences

        val colorInt = requireArguments().getInt("color")
        val composeColor = Color(colorInt)
        originalNotes = requireArguments().getString("notes") ?: ""
        originalValue = requireArguments().getInt("value")
        currentNotes = originalNotes

        val dialog = Dialog(requireContext())
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        val themeSwitcher = AndroidThemeSwitcher(requireContext(), prefs)

        val view = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme(theme = themeSwitcher.currentTheme) {
                    var notes by remember { mutableStateOf(originalNotes) }

                    CheckmarkDialogContent(
                        notes = notes,
                        onNotesChanged = {
                            notes = it
                            currentNotes = it
                        },
                        onAction = { value ->
                            dismissedViaSaveAction = true
                            onToggle(value, notes)
                            dialog.dismiss()
                        },
                        primaryColor = composeColor,
                        skipEnabled = prefs.isSkipEnabled,
                        unknownEnabled = prefs.areQuestionMarksEnabled
                    )
                }
            }
        }

        dialog.setContentView(view)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        if (!dismissedViaSaveAction) {
            if (currentNotes != originalNotes) {
                onToggle(originalValue, currentNotes)
            }
        }
        onDismiss()
    }
}
