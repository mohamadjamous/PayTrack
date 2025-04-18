package com.app.paytrack.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R

@Composable
fun CustomTabs(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {

    val list = listOf(stringResource(id = R.string.expense), stringResource(id = R.string.income))

    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = colorResource(id = R.color.dark_green),
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .padding(0.5.dp),
        indicator = { tabPositions: List<TabPosition> -> Box {} }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex == index

            Tab(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (selected) Color.White else colorResource(id = R.color.dark_green)
                    ),
                selected = selected,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = text,
                        color = if (selected) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            )
        }
    }
}
