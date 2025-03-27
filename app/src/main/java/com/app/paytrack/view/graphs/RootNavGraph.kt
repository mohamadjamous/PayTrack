package com.app.paytrack.view.graphs

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.paytrack.model.Graph
import com.app.paytrack.model.Screen
import com.app.paytrack.view.screens.OnBoardingScreen
import com.app.paytrack.view.screens.SignInScreen
import com.app.paytrack.view.screens.WelcomeScreen
import com.app.paytrack.view.sign_in.SignInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import androidx.lifecycle.lifecycleScope
import com.app.paytrack.R
import com.app.paytrack.view.main.HomeScreen
import com.app.paytrack.view.screens.SignUpScreen
import com.app.paytrack.view.sign_in.GoogleAuthUiClient


@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient
) {

    val context = LocalContext.current
    val firstTime = getSharedPreferences(context = context)
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleScope = remember(lifecycleOwner) { lifecycleOwner.lifecycleScope }


    // Check if it's first time
    val startDestination = if (firstTime == 0 || firstTime == -1) {
        Screen.Welcome
    } else {
        Screen.SignIn
    }

    if (firstTime == 0 || firstTime == -1) {
        saveToPreferences(context = context, value = 1)
    }

    NavHost(
        navController = navController,
        startDestination = Graph.Auth
    ) {

        // On boarding nav graph
        navigation<Graph.Auth>(
            startDestination = startDestination
        ) {

            composable<Screen.Welcome> {

                WelcomeScreen(navController = navController)
            }

            composable<Screen.OnBoarding> {
                OnBoardingScreen(
                    navController = navController
                )
            }

            composable<Screen.SignIn> {

                val viewModel = viewModel<SignInViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(key1 = Unit) {
                    if (googleAuthUiClient.getSignedInUser() != null) {

                        // Navigate to graph instead of one composable screen
                        navController.navigate(Graph.Main)
                    }
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {

                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

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

                SignInScreen(
                    navController = navController,
                    state = state,
                    onGoogleSignInClick = {
                        lifecycleScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                )
            }


            composable<Screen.SignUp> {

                val viewModel = viewModel<SignInViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(key1 = Unit) {
                    if (googleAuthUiClient.getSignedInUser() != null) {

                        // Navigate to graph instead of one composable screen
                        navController.navigate(Graph.Main)
                    }
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {

                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

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

                SignUpScreen(
                    navController = navController,
                    state = state,
                    onGoogleSignInClick = {
                        lifecycleScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                )
            }
        }

        navigation<Graph.Main>(
            startDestination = Screen.Home
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    onSignOutClick = {
                        lifecycleScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(
                                context,
                                "Signed out",
                                Toast.LENGTH_LONG
                            ).show()

                            navController.popBackStack()
                        }
                    }
                )
            }

        }

    }

}

fun saveToPreferences(context: Context, value: Int) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("is_first_time", value).apply()
}

fun getSharedPreferences(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("is_first_time", -1)
}




