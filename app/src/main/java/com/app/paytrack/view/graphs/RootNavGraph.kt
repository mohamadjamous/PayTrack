package com.app.paytrack.view.graphs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.paytrack.model.Graph
import com.app.paytrack.model.Screen
import com.app.paytrack.view.screens.OnBoardingScreen
import com.app.paytrack.view.screens.WelcomeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val context = LocalContext.current
    val firstTime = getSharedPreferences(context = context)

    // Check if it's first time
    val startDestination = if (firstTime == 0 || firstTime == -1 ){
        Screen.Welcome
    }else{
        Screen.SignIn
    }

    if (firstTime == 0 || firstTime == -1){
        saveToPreferences(context = context, value = 1)
    }

    NavHost(
        navController = navController,
        startDestination = Graph.Auth){

        // On boarding nav graph
        navigation<Graph.Auth>(
            startDestination = startDestination
        ){

            composable<Screen.Welcome>{

                WelcomeScreen(navController = navController)
            }

            composable<Screen.OnBoarding>{
                OnBoardingScreen(
                    navController = navController
                )
            }

            composable<Screen.SignIn>{
                OnBoardingScreen(
                    navController = navController
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




