package com.app.paytrack.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DailySpendingBarChart(
    modifier: Modifier = Modifier,
    dailySpending: List<Int>,
) {
    val maxSpending = 100f

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(dailySpending) { index, amount ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .width(20.dp)
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier
                        .height((amount / maxSpending * 80).dp) // max bar height = 80.dp
                        .width(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${index + 1}",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}