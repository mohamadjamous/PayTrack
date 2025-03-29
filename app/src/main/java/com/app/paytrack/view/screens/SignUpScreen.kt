package com.app.paytrack.view.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.paytrack.R
import com.app.paytrack.model.Graph
import com.app.paytrack.model.Screen
import com.app.paytrack.view.components.CustomButton
import com.app.paytrack.view.components.CustomDialog
import com.app.paytrack.view.components.CustomTextField
import com.app.paytrack.view.sign_in.GoogleAuthUiClient
import com.app.paytrack.view.sign_in.SignInState
import com.app.paytrack.viewmodel.SignUpViewModel
import java.util.Locale

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onGoogleSignInClick: () -> Unit,
    state: SignInState,
    viewModel: SignUpViewModel,
    googleAuthUiClient: GoogleAuthUiClient? = null,
) {

    val context = LocalContext.current

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var conPassword by remember {
        mutableStateOf("")
    }


    var showDialog by remember {
        mutableStateOf(false)
    }

    // Handle sign up error
    LaunchedEffect(state.signInError) {
        state.signInError?.let { error ->
            showDialog = false  // Hide dialog on error
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    // Handle sign up success
    LaunchedEffect(state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            showDialog = false  // Hide dialog on success
            Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Home)
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (googleAuthUiClient?.getSignedInUser() != null) {

            // Navigate to graph instead of one composable screen
            navController.navigate(Graph.Main)
        }
    }

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful!",
                Toast.LENGTH_LONG
            ).show()

            // Navigate to graph instead of one composable screen
            navController.navigate(Graph.Main)
            viewModel.resetState()
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {


            Text(
                text = stringResource(id = R.string.sign_up),
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
                color = colorResource(id = R.color.dark_green)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    hint = stringResource(id = R.string.name),
                    leadingIcon = Icons.Outlined.Person
                ) {
                    name = it
                }

                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    hint = stringResource(id = R.string.email),
                    leadingIcon = Icons.Outlined.Email,
                    passwordVisible = false
                ) {
                    email = it
                }

                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    hint = stringResource(id = R.string.password),
                    leadingIcon = Icons.Outlined.Lock,
                    passwordVisible = true
                ) {
                    password = it
                }
                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = conPassword,
                    hint = stringResource(id = R.string.confirm_password),
                    leadingIcon = Icons.Outlined.Lock,
                    passwordVisible = true
                ) {
                    conPassword = it
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = stringResource(id = R.string.sign_up).uppercase(Locale.ROOT)
            ) {
                showDialog = true

                val result = viewModel.validateInput(
                    email = email,
                    name = name,
                    password = password,
                    confirmPassword = conPassword
                )

                // Valid user input
                if (result == null) {
                    viewModel.signUpUserEmailPassword(
                        name = name,
                        email = email,
                        password = password
                    )
                }
                // Not Valid
                else {
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                }
            }

            OrDivider(
                modifier = Modifier.padding(top = 35.dp)
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    text = stringResource(id = R.string.sign_in_with),
                    color = colorResource(id = R.color.gray),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.height(15.dp))

                IconButton(
                    content = {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    },
                    onClick = {
                        showDialog = true
                        onGoogleSignInClick()
                    }
                )

                Row(
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.already_have_account),
                        color = colorResource(id = R.color.gray),
                        fontSize = 17.sp
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .clickable {
                                navController.popBackStack()
                            },
                        text = stringResource(id = R.string.sign_in),
                        color = colorResource(id = R.color.gray),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

            }

        }


        CustomDialog(show = showDialog)

    }

}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview(modifier: Modifier = Modifier) {
    SignUpScreen(
        navController = rememberNavController(),
        state = SignInState(isSignInSuccessful = false, signInError = null),
        onGoogleSignInClick = {},
        viewModel = SignUpViewModel()
    )
}