package org.isoron.uhabits.activities.common.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.isoron.uhabits.R
import org.isoron.uhabits.core.models.Entry.Companion.NO
import org.isoron.uhabits.core.models.Entry.Companion.SKIP
import org.isoron.uhabits.core.models.Entry.Companion.UNKNOWN
import org.isoron.uhabits.core.models.Entry.Companion.YES_MANUAL

@Composable
fun CheckmarkDialogContent(
    notes: String,
    habitName: String,
    onNotesChanged: (String) -> Unit,
    onAction: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    primaryColor: Color,
    skipEnabled: Boolean = true,
    unknownEnabled: Boolean = true
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.default_reminder_question))
        },
        text = {
            Column {
                Text(text = habitName, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = onNotesChanged,
                    label = { Text(stringResource(R.string.notes)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = { onAction(YES_MANUAL) },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = primaryColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.check)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.check))
            }
        },
        dismissButton = {
            Row {
                if (unknownEnabled) {
                    FilledTonalButton(
                        onClick = { onAction(UNKNOWN) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = stringResource(R.string.question)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.question))
                    }
                }

                if (skipEnabled) {
                    Button(
                        onClick = { onAction(SKIP) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = stringResource(R.string.skip_day)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.skip_day))
                    }
                }

                FilledTonalButton(
                    onClick = { onAction(NO) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.no)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.no))
                }
            }
        }
    )
}
