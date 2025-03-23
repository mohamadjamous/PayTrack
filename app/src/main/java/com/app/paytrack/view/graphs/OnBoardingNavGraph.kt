package com.app.paytrack.view.graphs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.paytrack.model.Screen
import com.app.paytrack.view.screens.OnBoardingScreen
import com.app.paytrack.view.screens.WelcomeScreen

@Composable
fun OnBoardingNavGraph(modifier: Modifier = Modifier, navController: NavHostController) {

    val context = LocalContext.current
    val firstTime = getSharedPreferences(context = context)
    println("FirstTimeValue: $firstTime")

    // Check if it's first time
    val startDestination = if (firstTime == 0 || firstTime == -1 ){
        Screen.Welcome.route
    }else{
        Screen.SignIn.route
    }

    if (firstTime == 0 || firstTime == -1){
        saveToPreferences(context = context, value = 1)
    }

    NavHost(
        navController = navController, startDestination = startDestination){

        composable(route = Screen.Welcome.route){
            WelcomeScreen()
        }

        composable(route = Screen.OnBoarding.route){
            OnBoardingScreen()
        }

        composable(route = Screen.SignIn.route){
            OnBoardingScreen()
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