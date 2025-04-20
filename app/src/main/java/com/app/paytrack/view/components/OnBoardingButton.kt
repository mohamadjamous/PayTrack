package com.app.paytrack.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R

@Composable
fun OnBoardingButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit) {

    Button(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ){

       Row(
           modifier = Modifier.fillMaxWidth(),
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.SpaceBetween
       ) {

           Spacer(modifier = Modifier)

           Text(
               modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
               text = text,
               fontWeight = FontWeight.Bold,
               textAlign = TextAlign.Center,
               color = Color.White,
               fontSize = 17.sp
           )

           Icon(
               painter = painterResource(id = R.drawable.arrow_right),
               contentDescription = null,
               tint = colorResource(id = R.color.white)
           )
       }

    }

}

@Preview(showBackground = true)
@Composable
fun OnBoardingButtonPreview(modifier: Modifier = Modifier) {
    OnBoardingButton(
        text = "Next",
        onClick = {}
    )
}