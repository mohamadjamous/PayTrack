package com.app.paytrack.view.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(showBottomSheet: Boolean, onDismiss: () -> Unit) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val scope = rememberCoroutineScope()

    // Launch the bottom sheet when showBottomSheet is true
    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        onDismissRequest = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                onDismiss() // Close the bottom sheet when dismissed
            }
        },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        // Sheet content
        Button(onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                onDismiss() // Hide and update the state when the button is clicked
            }
        }) {

        }
    }
}
