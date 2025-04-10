package com.app.paytrack.view.main

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R
import com.app.paytrack.model.Category
import com.app.paytrack.view.components.BottomSheet
import com.app.paytrack.view.components.Categories
import com.app.paytrack.view.components.MonthlyExpenses
import com.app.paytrack.view.components.MostUsedCategories
import com.app.paytrack.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,

    date: String,
    viewModel: HomeViewModel
    ) {

    // State to toggle bottom sheet visibility
    var isBottomSheetVisible by remember { mutableStateOf(false) }


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
            name = "Lorem",
            value = "7,000€",
            date = "June 7",
            desc = "Dum & Simple"
        ),
        Category(
            painter = painterResource(id = R.drawable.person),
            name = "Lorem",
            value = "7,000€",
            date = "June 7",
            desc = "Dum & Simple"
        ),
        Category(
            painter = painterResource(id = R.drawable.person),
            name = "Lorem",
            value = "7,000€",
            date = "June 7",
            desc = "Dum & Simple"
        )
    )

    val balanceState = viewModel.balance.collectAsState()
    val context = LocalContext.current

    var balance by remember { mutableStateOf(0.0)}
    var showBalanceProgress by remember { mutableStateOf(true)}

    LaunchedEffect(balanceState.value) {

        if (balanceState.value > 0.0){
            balance = balanceState.value
            showBalanceProgress = false
        }else{
            showBalanceProgress = false
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
        ) {


            // Current date "Sun, June 5"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                // Centered Text

               Column (
                   horizontalAlignment = Alignment.CenterHorizontally,
               ) {

                  Box (
                      contentAlignment = Alignment.Center,
                      modifier = Modifier
                  )
                  {

                      Text(
                          modifier = Modifier.fillMaxWidth(),
                          text = "$$balance",
                          fontWeight = FontWeight.Bold,
                          color = colorResource(id = R.color.green),
                          fontSize = 27.sp,
                          textAlign = TextAlign.Center
                      )

                      if (showBalanceProgress){
                          CircularProgressIndicator(
                              color = colorResource(id = R.color.green)
                          )
                      }

                  }

                   Text(
                       modifier = Modifier.padding(top = 10.dp),
                       text = date,
                       color = colorResource(id = R.color.dark_green),
                       fontSize = 16.sp,
                       textAlign = TextAlign.Center
                   )
               }

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
                modifier = Modifier.padding(top = 15.dp),
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
                modifier = Modifier.padding(top = 15.dp),
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

            Categories(
                modifier = Modifier.padding(top = 15.dp),
                list = list
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

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd) // Align the FAB to the bottom right
                .padding(bottom = 100.dp, end = 15.dp), // Optional padding to give some space from edges
            onClick = {
                isBottomSheetVisible = true
            },
            shape = CircleShape,
        ) {
            Icon(Icons.Filled.Add, "Floating action button.", tint = Color.Black)
        }


        // Show BottomSheet if isBottomSheetVisible is true
        if (isBottomSheetVisible) {
            BottomSheet(
                showBottomSheet = isBottomSheetVisible,
                onDismiss = { isBottomSheetVisible = false },
                onSubmit = { type, amount, category ->

                    viewModel.updateBalance(amount = amount, type = type)

                    // Remove to update with state
                    isBottomSheetVisible = false


                }
            )
        }


    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    HomeScreen(
        date = "Sun, June 5",
        viewModel = HomeViewModel()
    )
}