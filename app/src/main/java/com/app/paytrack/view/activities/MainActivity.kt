package com.app.paytrack.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.app.paytrack.view.graphs.RootNavGraph
import com.app.paytrack.view.sign_in.GoogleAuthUiClient
import com.app.paytrack.view.ui.theme.PayTrackTheme
import com.app.paytrack.viewmodel.ThemeViewModel
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            PayTrackTheme(isDarkTheme = isDarkTheme) {
                RootNavGraph(
                    navController = rememberNavController(),
                    googleAuthUiClient = googleAuthUiClient,
                    onToggleTheme = { themeViewModel.toggleTheme() }
                )
            }
        }

    }
}


