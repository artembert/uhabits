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

package org.isoron.uhabits.activities.common.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import org.isoron.uhabits.core.ui.views.DarkTheme
import org.isoron.uhabits.core.ui.views.PureBlackTheme
import org.isoron.uhabits.core.ui.views.Theme
import org.isoron.uhabits.utils.StyledResources

@Composable
fun AppTheme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val sres = StyledResources(context)
    val isDark = theme is DarkTheme || theme is PureBlackTheme

    val surfaceColor = Color(sres.getColor(com.google.android.material.R.attr.colorSurface))
    val background = Color(sres.getColor(android.R.attr.windowBackground))
    val onSurface = Color(sres.getColor(com.google.android.material.R.attr.colorOnSurface))
    val primary = Color(sres.getColor(androidx.appcompat.R.attr.colorPrimary))
    val onPrimary = Color(sres.getColor(com.google.android.material.R.attr.colorOnPrimary))
    val secondaryContainer = Color(sres.getColor(com.google.android.material.R.attr.colorSecondaryContainer))
    val onSecondaryContainer = Color(sres.getColor(com.google.android.material.R.attr.colorOnSecondaryContainer))
    val surfaceContainer = Color(sres.getColor(com.google.android.material.R.attr.colorSurfaceContainer))
    val surfaceVariant = Color(sres.getColor(com.google.android.material.R.attr.colorSurfaceVariant))
    val onSurfaceVariant = Color(sres.getColor(com.google.android.material.R.attr.colorOnSurfaceVariant))

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            surface = surfaceColor,
            onSurface = onSurface,
            background = background,
            surfaceContainer = surfaceContainer,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            surface = surfaceColor,
            onSurface = onSurface,
            background = background,
            surfaceContainer = surfaceContainer,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
