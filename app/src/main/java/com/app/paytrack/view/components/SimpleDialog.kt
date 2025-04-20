package com.app.paytrack.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.paytrack.R

@Composable
fun SimpleDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    // Dialog Box
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = { onCancel() }, // Dismiss on outside click
        title = {
            Text(text = stringResource(id = R.string.delete_account), style = MaterialTheme.typography.headlineLarge)
        },
        text = {
            Text(text = stringResource(id = R.string.delete_account_desc))
        },
        confirmButton = {
            CustomButton(
                modifier = Modifier.width(120.dp).padding(top = 30.dp, start = 5.dp, end = 5.dp),
                text = stringResource(id = R.string.confirm),
                textSize = 15
            ) {
                onConfirm()
            }
        },
        dismissButton = {
            CustomButton(
                modifier = Modifier.width(120.dp).padding(top = 30.dp, start = 5.dp, end = 5.dp),
                text = stringResource(id = R.string.cancel),
                textSize = 15
            ) {
                onCancel()
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun SimpleDialogPreview(modifier: Modifier = Modifier) {
    SimpleDialog(
        onCancel = {},
        onConfirm = {}
    )
}
