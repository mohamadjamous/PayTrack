package com.app.paytrack.view.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.paytrack.model.CategoryData
import com.app.paytrack.model.ChartsData
import com.app.paytrack.view.components.ChartsTable
import com.app.paytrack.view.components.CustomTabs
import com.app.paytrack.viewmodel.ChartsViewModel

@Composable
fun ChartsScreen(
    modifier: Modifier = Modifier,
    viewModel: ChartsViewModel
) {

    var selectedIndex by remember { mutableIntStateOf(0) }
    val data = viewModel.data.collectAsState().value
    var selectedData by remember {
        mutableStateOf(data.expenseData)
    }
    var selectedName by remember {
        mutableStateOf("")
    }

    val loading = viewModel.loading.collectAsState().value

    LaunchedEffect(selectedIndex, data) {
        if (selectedIndex == 0) {
            selectedName = "Expenses"
            selectedData = data.expenseData
        } else {
            selectedName = "Income"
            selectedData = data.incomeData
        }
    }


    if (loading) {
        // Show progress while loading
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {


            // top taps
            CustomTabs(
                modifier = Modifier.padding(top = 50.dp, start = 20.dp, end = 20.dp),
                selectedIndex = selectedIndex,
                onTabSelected = {
                    selectedIndex = it
                }
            )

            // charts table
            ChartsTable(
                modifier = Modifier.padding(top = 30.dp, start = 15.dp, end = 15.dp),
                data = selectedData,
                chartName = selectedName
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Bar chart
            SimpleBarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(start = 16.dp, end = 16.dp),
                data = selectedData
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Pie chart
            SimplePieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(start = 16.dp, end = 16.dp),
                data = selectedData
            )


        }
    }
}

@Composable
fun SimpleBarChart(modifier: Modifier = Modifier, data: List<CategoryData>) {
    val maxValue = data.maxOfOrNull { it.value } ?: 1

    Column(modifier = modifier) {
        data.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier.width(80.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width((item.value.toFloat() / maxValue * 200).dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.value.toString())
            }
        }
    }
}

@Composable
fun SimplePieChart(modifier: Modifier = Modifier, data: List<CategoryData>) {

    val total = data.sumOf { it.value }
    val colors = listOf(
        Color(0xFFEF5350), Color(0xFFAB47BC), Color(0xFF5C6BC0),
        Color(0xFF29B6F6), Color(0xFF66BB6A), Color(0xFFFFCA28)
    )

    Row(modifier = modifier.padding(top = 20.dp)) {

        // Pie Chart
        Canvas(modifier = Modifier.size(200.dp)) {
            var startAngle = -90f
            data.forEachIndexed { index, item ->
                val sweepAngle = (item.value.toFloat() / total) * 360f
                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true
                )
                startAngle += sweepAngle
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Legend
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            data.forEachIndexed { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(colors[index % colors.size], shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${item.name} (${item.value})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun ChartsScreenPreview(modifier: Modifier = Modifier) {
    ChartsScreen(
        viewModel = ChartsViewModel()
    )

}