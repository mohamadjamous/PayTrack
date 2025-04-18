package com.app.paytrack.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R

@Composable
fun ChartItem(
    modifier: Modifier = Modifier,
    name: String,
    value: Int
) {
    val barHeightRatio = (value.coerceIn(0, 200)) / 200f

    println("ValueBeingPassed: $value")

    Column(
        modifier = modifier
            .width(58.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {


        Spacer(modifier = Modifier.height(4.dp))

        // Chart bar
        Box(
            modifier = Modifier
                .height(100.dp)
                .width(20.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(barHeightRatio)
                    .background(colorResource(id = R.color.dark_green), shape = RoundedCornerShape(6.dp))
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Category name
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(48.dp),
            fontSize = 8.sp
        )
    }
}



@Preview(showBackground = true)
@Composable
fun ChartItemPreview(modifier: Modifier = Modifier) {
    ChartItem(
        name = "home",
        value = 5
    )
}