package com.app.paytrack.view.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.paytrack.view.components.OnBoardingButtonPreview

@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier) {

    Text(text = "On Boarding Screen")
}


@Preview(showBackground = true)
@Composable
fun OnBoardingScreenPreview(modifier: Modifier = Modifier) {

    OnBoardingScreen()
}