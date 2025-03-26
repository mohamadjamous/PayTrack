package com.app.paytrack.view.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.paytrack.R

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String = "",
    leadingIcon: ImageVector? = null,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {

        },
        placeholder = {
            Text(text = hint)
        },
        leadingIcon = {
            if (leadingIcon != null)
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = colorResource(id = R.color.dark_green)
                )
        },
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = Color.Gray,
            focusedTextColor = colorResource(id = R.color.gray),
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            focusedIndicatorColor = colorResource(id = R.color.dark_green),
        ),
        shape = RoundedCornerShape(12.dp)

    )

}


@Preview(showSystemUi = true)
@Composable
fun CustomTextFieldPreview(modifier: Modifier = Modifier) {
    CustomTextField(
        value = "",
        onValueChange = {

        },
        leadingIcon = Icons.Outlined.Email
    )
}