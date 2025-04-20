package com.app.paytrack.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R
import com.app.paytrack.utlis.categories

@Composable
fun BottomSheetContent(
    onSubmit: (type: String, amount: String, category: String?) -> Unit,
) {

    var selectedTab by remember { mutableStateOf(0) } // 0 = Income, 1 = Expense
    val tabs = listOf("Income", "Expense")

    var incomeAmount by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(text = title, color = Color.White) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (selectedTab) {
            0 -> { // Income

                CustomTextField(
                    value = incomeAmount,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) incomeAmount = it
                    },
                    hint = "Amount",
                    leadingIcon1 = { Text("$", fontWeight = FontWeight.Bold,
                         color = MaterialTheme.colorScheme.primary) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )


                Spacer(modifier = Modifier.height(35.dp))

                CustomButton(
                    modifier = Modifier.padding(start = 80.dp, end = 80.dp),
                    text = "Submit"
                ) {
                    // Handle income submit
                    onSubmit("0", incomeAmount, null)
                }
            }

            1 -> { // Expense
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    items(categories) { (name, icon) ->
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { selectedCategory = name },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedCategory == name) Color.Black else Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = name,
                                    tint = if (selectedCategory == name) Color.White else Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(name, color = Color.White, fontSize = 12.sp)
                        }
                    }
                }

                selectedCategory?.let {
                    Spacer(modifier = Modifier.height(15.dp))


                    CustomTextField(
                        value = expenseAmount,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() }) expenseAmount = it
                        },
                        hint = "Amount",
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        leadingIcon1 = { Text("$", fontWeight = FontWeight.Bold) }
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    CustomButton(
                        modifier = Modifier.padding(start = 80.dp, end = 80.dp),
                        text = "Submit"
                    ) {
                        // Handle income submit
                        onSubmit("1", expenseAmount, selectedCategory)
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview(modifier: Modifier = Modifier) {
    BottomSheetContent(onSubmit = { _, _, _ -> })
}