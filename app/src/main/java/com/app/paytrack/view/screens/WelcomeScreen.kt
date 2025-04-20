package com.app.paytrack.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.app.paytrack.model.Screen
import com.app.paytrack.view.components.OnBoardingButton

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp)
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,

    ) {

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier.size(70.dp),
                painter = painterResource(id = R.drawable.pattern),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(120.dp))

            Image(
                modifier = Modifier.size(130.dp),
                painter = painterResource(id = R.drawable.coin_iccon),
                contentDescription = null
            )

        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .padding(horizontal = 20.dp),
            text = stringResource(id = R.string.welcome_text),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 50.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.weight(1f))

        OnBoardingButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            text = stringResource(id = R.string.next)
        ) {
            navController.navigate(Screen.OnBoarding)
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun WelcomeScreenPreview(modifier: Modifier = Modifier) {
    WelcomeScreen(
        navController = rememberNavController()
    )
}