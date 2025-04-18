package com.app.paytrack.view.graphs

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.app.paytrack.view.main.BottomNavigationBar
import com.app.paytrack.view.main.ChartsScreen
import com.app.paytrack.view.main.HomeScreen
import com.app.paytrack.view.main.ProfileScreen
import com.app.paytrack.view.screens.ForgotPasswordScreen
import com.app.paytrack.view.screens.SettingsScreen
import com.app.paytrack.view.screens.SignUpScreen
import com.app.paytrack.view.sign_in.CreateAccountScreen
import com.app.paytrack.view.sign_in.GoogleAuthUiClient
import com.app.paytrack.viewmodel.ChartsViewModel
import com.app.paytrack.viewmodel.CreateAccountViewModel
import com.app.paytrack.viewmodel.HomeViewModel
import com.app.paytrack.viewmodel.ProfileViewModel
import com.app.paytrack.viewmodel.SettingsViewModel
import com.app.paytrack.viewmodel.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    onToggleTheme : () -> Unit
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

    var isMainScreen by remember {
        mutableStateOf(false)
    }

    if (currentUser != null) {
        // User is signed in, navigate to the main screen
        isMainScreen = true
    }

    // Save to preferences if it's the first time
    if (firstTime == 0 || firstTime == -1) {
        saveToPreferences(context = context, value = 1)
    }

    // Main Scaffold UI
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Only show BottomNavigationBar when in Graph.Main
            if (isMainScreen) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->

        // NavHost with conditional start destination based on `isMainScreen`

        NavHost(
            navController = navController,
            startDestination = if (isMainScreen) {
                Graph.Main
            } else {
                Graph.Auth
            },
        ) {

            // On boarding nav graph
            navigation<Graph.Auth>(
                startDestination = startDestination
            ) {

                composable<Screen.Welcome>(
                    enterTransition = { slideInHorizontally() },
                    exitTransition = { slideOutHorizontally() }
                ) {

                    WelcomeScreen(navController = navController)
                }

                composable<Screen.OnBoarding>(
                    enterTransition = { slideInHorizontally() },
                    exitTransition = { slideOutHorizontally() }
                ) {
                    OnBoardingScreen(
                        navController = navController
                    )
                }

                composable<Screen.SignIn>(
                    enterTransition = { slideInHorizontally() },
                    exitTransition = { slideOutHorizontally() }
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
                        onRegister = {
                            isMainScreen = true
                            Toast.makeText(context, "Sign in successful!", Toast.LENGTH_LONG).show()
                            navController.navigate(Graph.Main) {
                                popUpTo(Graph.Auth) { inclusive = true } // Prevent back navigation
                            }
                        },
                        viewModel = viewModel
                    )
                }


                composable<Screen.SignUp>(
                    enterTransition = { slideInHorizontally() },
                    exitTransition = { slideOutHorizontally() }
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

                composable<Screen.ForgotPassword>(
                    enterTransition = { slideInHorizontally() },
                    exitTransition = { slideOutHorizontally() }
                ) {

                    val viewModel = viewModel<SignInViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    ForgotPasswordScreen(
                        navController = navController,
                        state = state,
                        viewModel = viewModel
                    )
                }

                composable<Screen.CreateAccount>(
                    enterTransition = { slideInHorizontally() },
                    exitTransition = { slideOutHorizontally() }
                ) {

                    val viewModel = viewModel<CreateAccountViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    CreateAccountScreen(
                        viewModel = viewModel,
                        state = state,
                        onAccountCreate = {

                            isMainScreen = true
                            navController.navigate(Graph.Main) {
                                popUpTo(Graph.Auth) { inclusive = true } // Prevent back navigation
                            }
                        }
                    )


                }

            }

            navigation<Graph.Main>(
                startDestination = Screen.Home
            ) {

                val date: String = LocalDate.now().format(
                    DateTimeFormatter.ofPattern("EEE, MMMM d", Locale.ENGLISH)
                )


                composable<Screen.Home>(
                    enterTransition = { fadeIn(animationSpec = tween(durationMillis = 150)) },
                    exitTransition = { fadeOut(animationSpec = tween(durationMillis = 150)) }
                ) {

                    // init view model
                    val viewModel = viewModel<HomeViewModel>()

                    HomeScreen(
                        date = date,
                        viewModel = viewModel,
                        onSettingsClick = {
//                            isMainScreen = true
                            navController.navigate(Screen.Settings)
                        }
                    )
                }

                composable<Screen.Charts>(
                    enterTransition = { fadeIn(animationSpec = tween(durationMillis = 150)) },
                    exitTransition = { fadeOut(animationSpec = tween(durationMillis = 150)) }
                ) {

                    // init view model
                    val viewModel = viewModel<ChartsViewModel>()

                    ChartsScreen(
                        viewModel = viewModel
                    )
                }

                composable<Screen.Profile>(
                    enterTransition = { fadeIn(animationSpec = tween(durationMillis = 150)) },
                    exitTransition = { fadeOut(animationSpec = tween(durationMillis = 150)) }
                ) {

                    // init view model
                    val viewModel = viewModel<ProfileViewModel>()

                    ProfileScreen(
                        viewModel = viewModel,
                        onDeleteAccount = {

                            isMainScreen = false
                            navController.navigate(Graph.Auth) {
                                popUpTo(Graph.Auth) { inclusive = true } // Prevent back navigation
                            }

                        },
                        onSignOutClick = {

                            lifecycleScope.launch {
                                googleAuthUiClient.signOut()
                                Toast.makeText(
                                    context,
                                    "Signed out",
                                    Toast.LENGTH_LONG
                                ).show()
                                isMainScreen = false

                                navController.navigate(Graph.Auth) {
                                    popUpTo(Graph.Auth) {
                                        inclusive = true
                                    } // Prevent back navigation
                                }
                            }
                        },
                    )
                }

                composable<Screen.Settings>(
                    enterTransition = { fadeIn(animationSpec = tween(durationMillis = 150)) },
                    exitTransition = { fadeOut(animationSpec = tween(durationMillis = 150)) }
                ) {

                    // init view model
                    val viewModel = viewModel<SettingsViewModel>()

                    SettingsScreen(
                        onBackClick = {
//                        isMainScreen = false
                            navController.popBackStack()
                        },
                        viewModel = viewModel,
                        onToggleTheme = {
                            onToggleTheme()
                        }
                    )
                }

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




