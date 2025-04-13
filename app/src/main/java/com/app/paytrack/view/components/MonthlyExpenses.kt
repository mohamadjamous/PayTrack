package com.app.paytrack.view.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyExpenses(
    modifier: Modifier = Modifier,
    months: List<String>,
    monthlySpendingData: Map<String, List<Int>>
) {


    println("DebugValue: $months")

    println("DebugValue: ")
    monthlySpendingData.forEach { (month, dataList) ->
        println("$month -> $dataList")
    }

    val currentMonth = LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)

    var selectedMonth by remember { mutableStateOf(currentMonth) }

    val percentChange by remember { mutableStateOf("+921%") }


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
                items(months) { month ->
                    Text(
                        text = month.take(3),
                        color = Color.White,
                        fontWeight = if (month == selectedMonth) FontWeight.Bold else FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { selectedMonth = month }
                            .padding(horizontal = 8.dp)
                            .background(
                                if (month == selectedMonth) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
            }

            // Selected month on the right
            Text(
                text = selectedMonth,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp)
            )


        }


        Text(
            modifier = Modifier.padding(top = 30.dp),
            text = "${monthlySpendingData[selectedMonth]?.sum() ?: 0}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )



        PercentageChangeBox(percentageText = percentChange)


        DailySpendingBarChart(
            modifier = Modifier.padding(top = 16.dp),
            dailySpending = monthlySpendingData[selectedMonth] ?: emptyList()
        )

    }
}


@Preview(showSystemUi = true)
@Composable
fun MonthlyExpensesPreview(modifier: Modifier = Modifier) {

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val monthlySpendingData = remember {
        months.associateWith { List(30) { (0..100).random() } }
    }
    MonthlyExpenses(
        modifier = Modifier.padding(16.dp),
        months = months,
        monthlySpendingData = monthlySpendingData
    )
}