package com.app.paytrack.view.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R
import com.app.paytrack.model.Category
import com.app.paytrack.view.components.MonthlyExpenses
import com.app.paytrack.view.components.MostUsedCategories

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSignOutClick: () -> Unit,
    date: String,

    ) {

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val monthlySpendingData = remember {
        months.associateWith { List(30) { (0..100).random() } }
    }

    val list = listOf(
        Category(
            painter = painterResource(id = R.drawable.person),
            "Lorem",
            value = "7,000€"
        ),
        Category(
            painter = painterResource(id = R.drawable.person),
            "Lorem",
            value = "7,000€"
        ),
        Category(
            painter = painterResource(id = R.drawable.person),
            "Lorem",
            value = "7,000€"
        ),
        Category(
            painter = painterResource(id = R.drawable.person),
            "Lorem",
            value = "7,000€"
        )
    )


    Column(
        modifier = Modifier.padding(15.dp)
            .verticalScroll(rememberScrollState()),
    ) {

        Text(
            modifier = Modifier.clickable {
                onSignOutClick()
            },
            text = "Sign out"
        )

        // Current date "Sun, June 5"
        Box(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            // Centered Text
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = date,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.dark_green),
                fontSize = 22.sp
            )

            // Icons aligned to the end
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                IconButton(
                    onClick = { /* your logic */ }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.notification),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }

                IconButton(
                    onClick = { /* your logic */ }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(50.dp))

        // Daily expenses chart
        Text(
            text = stringResource(id = R.string.monthly_expenses),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.dark_green)
        )

        MonthlyExpenses(
            modifier = Modifier.padding(top = 20.dp),
            months = months,
            monthlySpendingData = monthlySpendingData
        )


        Spacer(modifier = Modifier.height(50.dp))

        // Most used categories
        Text(
            text = stringResource(id = R.string.most_used_categories),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.dark_green)
        )

        MostUsedCategories(
            modifier = Modifier.padding(top = 20.dp),
            list = list
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Expenses categories
        Text(
            text = stringResource(id = R.string.expenses_categories),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.dark_green)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Reading icon
        Text(
            text = stringResource(id = R.string.reading),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.dark_green)
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    HomeScreen(
        onSignOutClick = {},
        date = "Sun, June 5"
    )
}