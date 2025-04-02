package com.app.paytrack.view.graphs

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.app.paytrack.viewmodel.SignInViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.app.paytrack.model.repo.AuthRepo
import com.app.paytrack.view.main.HomeScreen
import com.app.paytrack.view.screens.ForgotPasswordScreen
import com.app.paytrack.view.screens.SignUpScreen
import com.app.paytrack.view.sign_in.CreateAccountScreen
import com.app.paytrack.view.sign_in.GoogleAuthUiClient
import com.app.paytrack.viewmodel.CreateAccountViewModel
import com.app.paytrack.viewmodel.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth


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

    // Check if the user is signed in or signed up
    val currentUser = FirebaseAuth.getInstance().currentUser

    var isMainScreen = false
     if (currentUser != null) {
        // User is signed in, navigate to the main screen
        isMainScreen = true
    }

    // Save to preferences if it's the first time
    if (firstTime == 0 || firstTime == -1) {
        saveToPreferences(context = context, value = 1)
    }



    NavHost(
        navController = navController,
        startDestination = if (isMainScreen) {
            Graph.Main
        } else {
            Graph.Auth
        },
        enterTransition = { slideInHorizontally() },
        exitTransition = { slideOutHorizontally() }
    ) {

        // On boarding nav graph
        navigation<Graph.Auth>(
            startDestination = startDestination
        ) {

            composable<Screen.Welcome> {

                WelcomeScreen(navController = navController)
            }

            composable<Screen.OnBoarding>(

            ) {
                OnBoardingScreen(
                    navController = navController
                )
            }

            composable<Screen.SignIn>(

            ) {

                val viewModel = viewModel<SignInViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()


                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                // Sign in success, update UI with the signed-in user's information
                                signInResult.data?.isGoogleSignIn = true
                                viewModel.onSignInResult(signInResult)
                            }
                        } else {
                            viewModel.onSignInCancelled()
                        }
                    }
                )

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
                    },
                    googleAuthUiClient = googleAuthUiClient,
                    viewModel = viewModel
                )
            }


            composable<Screen.SignUp>(

            ) {

                val viewModel = viewModel<SignUpViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {

                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                // Sign in success, update UI with the signed-in user's information
                                signInResult.data?.isGoogleSignIn = true
                                viewModel.onSignInResult(signInResult)
                            }
                        } else {
                            viewModel.onSignInCancelled()
                        }
                    }
                )

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
                    },
                    viewModel = viewModel,
                    googleAuthUiClient = googleAuthUiClient
                )
            }

            composable<Screen.ForgotPassword> {

                val viewModel = viewModel<SignInViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                ForgotPasswordScreen(
                    navController = navController,
                    state = state,
                    viewModel = viewModel
                )
            }

            composable<Screen.CreateAccount>(

            ) {

                val viewModel = viewModel<CreateAccountViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                CreateAccountScreen(
                    viewModel = viewModel,
                    state = state,
                    navController = navController
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


suspend fun isUserCreateAccount(): Boolean {

    // access user firestore document
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val repo = AuthRepo()

    // Return if no user is signed in
    if (firebaseUser == null) {
        return false
    }

    val user = repo.getUserAccount(id = firebaseUser.uid)

    // check if user has at least one account
    // return true or false accordingly
    return user.account != null
}


fun saveToPreferences(context: Context, value: Int) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("is_first_time", value).apply()
}

fun getSharedPreferences(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("is_first_time", -1)
}




