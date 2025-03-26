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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.paytrack.R

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String = "",
    leadingIcon: ImageVector? = null,
    passwordVisible: Boolean = false,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
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
        visualTransformation = if (!passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = colorResource(id = R.color.dark_green),
            unfocusedTextColor = colorResource(id = R.color.dark_green),
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            focusedIndicatorColor = colorResource(id = R.color.dark_green),
        )

    )

}


@Preview(showSystemUi = true)
@Composable
fun CustomTextFieldPreview(modifier: Modifier = Modifier) {
    CustomTextField(
        value = "",
        onValueChange = {

        },
        leadingIcon = Icons.Outlined.Email,
        passwordVisible = true
    )
}