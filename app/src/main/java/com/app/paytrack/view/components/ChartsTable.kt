package com.app.paytrack.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.paytrack.R
import com.app.paytrack.model.CategoryData

@Composable
fun ChartsTable(
    modifier: Modifier = Modifier,
    data: List<CategoryData>,
    chartName: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(id = R.color.dark_green_1)),
    ) {
        Text(
            text = chartName,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp, top = 25.dp)
                ,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            data.forEach { item ->
                ChartItem(
                    name = item.name,
                    value = item.value
                )
            }
        }
    }
}



@Preview(showSystemUi = true)
@Composable
fun ChartsTablePreview(modifier: Modifier = Modifier) {
    ChartsTable(
        data = listOf(
            CategoryData(
                name = "home",
                value = 10
            ),
            CategoryData(
                name = "home",
                value = 50
            )
            , CategoryData(
                name = "home",
                value = 70
            )
        ),
        chartName = "Expenses"
    )
}