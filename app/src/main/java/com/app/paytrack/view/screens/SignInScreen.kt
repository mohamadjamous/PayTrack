package com.app.paytrack.view.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.paytrack.R
import com.app.paytrack.model.Graph
import com.app.paytrack.model.Screen
import com.app.paytrack.model.repo.AuthRepo
import com.app.paytrack.view.components.CustomButton
import com.app.paytrack.view.components.CustomDialog
import com.app.paytrack.view.components.CustomTextField
import com.app.paytrack.view.sign_in.GoogleAuthUiClient
import com.app.paytrack.view.sign_in.SignInState
import com.app.paytrack.viewmodel.SignInViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: SignInState,
    onGoogleSignInClick: () -> Unit,
    googleAuthUiClient: GoogleAuthUiClient? = null,
    viewModel: SignInViewModel? = null
) {

    val context = LocalContext.current


    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    // Handle errors from sign-in state
    LaunchedEffect(state.signInError) {

        showDialog = false
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    // Handle sign-in success for both Google and Email/Password
    LaunchedEffect(state.isSignInSuccessful) {

        showDialog = false
        if (state.isSignInSuccessful) {

            // Google sign in case
            if (state.isGoogleSignIn) {

                val firebaseUser = FirebaseAuth.getInstance().currentUser
                val email = firebaseUser?.email

                if (email != null) {

                    val isRegistered = AuthRepo().checkEmailExists(email)
                    if (isRegistered) {

                        Toast.makeText(context, "Sign in successful!", Toast.LENGTH_LONG).show()
                        navController.navigate(Graph.Main)
                    } else {
                        Toast.makeText(context, "Account is not registered!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(context, "Error retrieving user email!", Toast.LENGTH_LONG)
                        .show()
                }
            }
            // Email password sign in state
            else {
                Toast.makeText(context, "Sign in successful!", Toast.LENGTH_LONG).show()
                navController.navigate(Graph.Main) {
                    popUpTo(Graph.Auth) { inclusive = true } // Prevent back navigation
                }

            }
            viewModel?.resetState()
        }
    }

    LaunchedEffect(state.isSignInCancelled) {
        if (state.isSignInCancelled) {
            showDialog = false
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
                text = stringResource(id = R.string.sign_in),
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
                    value = email,
                    hint = stringResource(id = R.string.email),
                    leadingIcon = Icons.Outlined.Email
                ) {
                    email = it
                }

                Spacer(modifier = Modifier.height(35.dp))

                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    hint = stringResource(id = R.string.password),
                    leadingIcon = Icons.Outlined.Lock,
                    passwordVisible = true
                ) {
                    password = it
                }
            }

            Text(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.End)
                    .clickable {
                        navController.navigate(Screen.ForgotPassword)
                    },
                text = stringResource(id = R.string.forgot_password),
                color = colorResource(id = R.color.dark_green),
                fontWeight = FontWeight.Bold,
            )


            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = stringResource(id = R.string.sign_in).uppercase(Locale.ROOT)
            ) {

                showDialog = true
                val result = viewModel?.validateLoginInput(email = email, password = password)

                // Valid user input
                if (result == null) {
                    viewModel?.signInUserEmailPassword(email = email, password = password)
                } else {
                    Toast.makeText(
                        context,
                        result,
                        Toast.LENGTH_LONG
                    ).show()
                    showDialog = false
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

                Spacer(modifier = Modifier.height(25.dp))

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
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp),
                        text = stringResource(id = R.string.dont_have_account),
                        color = colorResource(id = R.color.gray),
                        fontSize = 17.sp
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 5.dp)
                            .clickable {
                                navController.navigate(Screen.SignUp)
                            },
                        text = stringResource(id = R.string.sign_up),
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

@Composable
fun OrDivider(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
    ) {

        Divider(modifier = Modifier.weight(1f), color = Color.Gray, thickness = 2.dp)

        Text(
            text = "OR",
            color = colorResource(id = R.color.dark_green),
            modifier = Modifier.padding(horizontal = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp
        )
        Divider(modifier = Modifier.weight(1f), color = Color.Gray, thickness = 2.dp)
    }
}


@Preview(showSystemUi = true)
@Composable
fun SignInScreenPreview(modifier: Modifier = Modifier) {
    SignInScreen(
        navController = rememberNavController(),
        state = SignInState(isSignInSuccessful = false, signInError = null),
        onGoogleSignInClick = {}
    )
}