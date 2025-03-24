package com.app.paytrack.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.app.paytrack.view.graphs.RootNavGraph
import com.app.paytrack.view.ui.theme.PayTrackTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PayTrackTheme {
                RootNavGraph(navController = rememberNavController())
            }
        }

    }
}


