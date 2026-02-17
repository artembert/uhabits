package org.isoron.uhabits.activities.common.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.isoron.uhabits.R
import org.isoron.uhabits.activities.habits.list.ListHabitsActivity
import org.isoron.uhabits.activities.settings.SettingsActivity

@Composable
fun FloatingToolbarNavigation(
    activeScreen: Class<*>,
    onListClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 3.dp,
        shadowElevation = 3.dp,
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavButton(
                selected = activeScreen == ListHabitsActivity::class.java,
                icon = Icons.AutoMirrored.Filled.List,
                contentDescription = stringResource(R.string.main_activity_title),
                label = stringResource(R.string.main_activity_title),
                onClick = onListClick
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedIconButton(
                onClick = onAddClick,
                shape = CircleShape,
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_habit)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            NavButton(
                selected = activeScreen == SettingsActivity::class.java,
                icon = Icons.Filled.Settings,
                contentDescription = stringResource(R.string.action_settings),
                label = stringResource(R.string.action_settings),
                onClick = onSettingsClick
            )
        }
    }
}

@Composable
private fun NavButton(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    label: String
) {
    val content: @Composable () -> Unit = {
        Icon(icon, contentDescription = contentDescription)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
    if (selected) {
        return FilledTonalButton(onClick = onClick, content = { content() })
    }
    return TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        content = { content() }
    )
}