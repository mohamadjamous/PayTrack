package com.app.paytrack.view.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R
import com.app.paytrack.model.MonthData
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyExpenses(
    modifier: Modifier = Modifier,
    months: List<MonthData>
) {

    val currentMonth = LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
    var selectedMonth by remember { mutableStateOf(months.find { it.name == currentMonth }) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(color = colorResource(id = R.color.dark_green_1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 25.dp), // Take up most of the space
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(months.size) {
                    Text(
                        text = months[it].name,
                        color = Color.White,
                        fontWeight = if (months[it].name == selectedMonth?.name) FontWeight.Bold else FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { selectedMonth = months[it] }
                            .padding(horizontal = 8.dp)
                            .background(
                                if (months[it] == selectedMonth) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
            }

            // Selected month on the right
            Text(
                text = selectedMonth?.name ?: "",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp)
            )


        }


        Text(
            modifier = Modifier.padding(top = 30.dp),
            text = "$${selectedMonth?.balance ?: 0.0}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )


        PercentageChangeBox(percentageText = selectedMonth?.percentage ?: "")


        DailySpendingBarChart(
            modifier = Modifier.padding(top = 16.dp),
            dailySpending = selectedMonth?.spendingData ?: emptyList()
        )

    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun MonthlyExpensesPreview(modifier: Modifier = Modifier) {

    MonthlyExpenses(
        modifier = Modifier.padding(16.dp),
        months = listOf(
            MonthData(
            id = 0,
            name = "Jan",
            spendingData = listOf(10, 20, 30),
            percentage = "+60%",
            balance = 100.0),

            MonthData(
                id = 1,
                name = "Jan",
                spendingData = listOf(10, 20, 30),
                percentage = "+60%",
                balance = 100.0),

            MonthData(
                id = 2,
                name = "Jan",
                spendingData = listOf(10, 20, 30),
                percentage = "+60%",
                balance = 100.0),

        )
    )
}