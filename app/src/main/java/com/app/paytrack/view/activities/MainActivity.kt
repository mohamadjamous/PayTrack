package com.app.paytrack.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.app.paytrack.view.graphs.RootNavGraph
import com.app.paytrack.view.sign_in.GoogleAuthUiClient
import com.app.paytrack.view.ui.theme.PayTrackTheme
import com.google.android.gms.auth.api.identity.Identity

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PayTrackTheme {
                RootNavGraph(
                    navController = rememberNavController(),
                    googleAuthUiClient = googleAuthUiClient
                )
            }
        }

    }
}


