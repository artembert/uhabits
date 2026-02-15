package org.isoron.uhabits.activities.habits.list.views

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
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.isoron.uhabits.R

@Composable
fun HabitsFloatingToolbar(isHabitsActive: Boolean = false) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 3.dp,
        shadowElevation = 3.dp,
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isHabitsActive) {
                FilledTonalButton(onClick = { /* Placeholder */ }) {
                    Icon(
                        Icons.AutoMirrored.Filled.List,
                        contentDescription = stringResource(R.string.main_activity_title)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.main_activity_title))
                }
            } else {
                TextButton(onClick = { /* Placeholder */ }) {
                    Icon(
                        Icons.AutoMirrored.Filled.List,
                        contentDescription = stringResource(R.string.main_activity_title)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.main_activity_title))
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            FloatingActionButton(
                onClick = { /* Placeholder */ },
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_habit)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(onClick = { /* Placeholder */ }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.action_settings)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.action_settings))
            }
        }
    }
}
