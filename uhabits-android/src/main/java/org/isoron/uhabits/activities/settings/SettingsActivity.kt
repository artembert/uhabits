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
package org.isoron.uhabits.activities.settings

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.res.stringResource
import org.isoron.uhabits.HabitsApplication
import org.isoron.uhabits.R
import org.isoron.uhabits.activities.AndroidThemeSwitcher
import org.isoron.uhabits.activities.common.views.AppTheme
import org.isoron.uhabits.activities.common.views.FloatingToolbarNavigation
import org.isoron.uhabits.activities.habits.edit.HabitTypeDialog
import org.isoron.uhabits.databinding.SettingsActivityBinding
import org.isoron.uhabits.utils.applyRootViewInsets

@OptIn(ExperimentalMaterial3Api::class)
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = (application as HabitsApplication).component
        val themeSwitcher = AndroidThemeSwitcher(this, component.preferences)
        themeSwitcher.apply()

        val binding = SettingsActivityBinding.inflate(LayoutInflater.from(this))
        binding.root.applyRootViewInsets()
        setContentView(binding.root)

        binding.toolbarCompose.setContent {
            AppTheme(theme = themeSwitcher.currentTheme) {
                TopAppBar(
                    title = { Text(stringResource(R.string.settings)) },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    windowInsets = WindowInsets.statusBars,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }

        binding.composeView.setContent {
            AppTheme(theme = themeSwitcher.currentTheme) {
                FloatingToolbarNavigation(
                    activeScreen = SettingsActivity::class.java,
                    onListClick = { finish() },
                    onAddClick = {
                        val dialog = HabitTypeDialog()
                        dialog.show(supportFragmentManager, "habitType")
                    }
                )
            }
        }
    }
}
