package com.app.paytrack.view.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.paytrack.R
import com.app.paytrack.view.components.BackButton
import com.app.paytrack.view.components.CustomButton
import com.app.paytrack.view.components.CustomDialog
import com.app.paytrack.view.components.CustomTextField
import com.app.paytrack.model.SignInState
import com.app.paytrack.viewmodel.SignInViewModel
import java.util.Locale

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: SignInState,
    viewModel: SignInViewModel
) {

    val context = LocalContext.current
    var email by remember {
        mutableStateOf("")
    }
    var showDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.passwordLinkError) {

        state.passwordLinkError?.let { error ->
            showDialog = false
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
            viewModel.resetState()
        }
    }

    LaunchedEffect(key1 = state.isPasswordLinkSuccessful) {

        if (state.isPasswordLinkSuccessful) {
            showDialog = false
            Toast.makeText(
                context,
                "Email sent successfully",
                Toast.LENGTH_LONG
            ).show()
            viewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            ) {

                BackButton {
                    navController.popBackStack()
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 70.dp),
                    text = stringResource(id = R.string.forgot_password_1),
                    color = colorResource(id = R.color.dark_green),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 5.dp)
            ) {


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    text = stringResource(id = R.string.forgot_password_desc),
                    color = colorResource(id = R.color.gray),
                    textAlign = TextAlign.Start,
                    fontSize = 17.sp
                )

                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 10.dp, end = 10.dp),
                    value = email,
                    hint = stringResource(id = R.string.email),
                    leadingIcon = Icons.Outlined.Email,
                    passwordVisible = false,
                ) {
                    email = it
                }
            }

            CustomButton(
                modifier = Modifier.padding(top = 30.dp, start = 15.dp, end = 15.dp),
                text = stringResource(id = R.string.next).uppercase(Locale.ROOT)
            ) {


                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()) {
                    Toast.makeText(context, "Invalid email", Toast.LENGTH_SHORT).show()
                } else {
                    showDialog = true

                    // Check if email is not Google account
                    viewModel.sendVerificationLink(email = email)
                }
            }

        }

        CustomDialog(show = showDialog)

    }

}


@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview(modifier: Modifier = Modifier) {
    ForgotPasswordScreen(
        navController = rememberNavController(),
        state = SignInState(),
        viewModel = SignInViewModel()
    )
}