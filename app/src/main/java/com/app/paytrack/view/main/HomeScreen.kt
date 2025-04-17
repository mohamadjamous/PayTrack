package com.app.paytrack.view.main

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.app.paytrack.model.ShimmerCategoryItem
import com.app.paytrack.utlis.Resource
import com.app.paytrack.view.components.BottomSheet
import com.app.paytrack.view.components.Categories
import com.app.paytrack.view.components.MonthlyExpenses
import com.app.paytrack.view.components.MostUsedCategories
import com.app.paytrack.viewmodel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    date: String,
    viewModel: HomeViewModel
) {

    // State to toggle bottom sheet visibility
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    val monthData by viewModel.monthlyExpenseState.collectAsState()
    val mostUsedCategories by viewModel.mostUsedCategories.collectAsState()
    val categories by viewModel.categoriesState.collectAsState()


    val balanceState = viewModel.balanceState.collectAsState()
    val updateBalanceState = viewModel.updateBalanceState.collectAsState().value
    val context = LocalContext.current

    var balance by remember { mutableStateOf(0.0) }
    var showBalanceProgress by remember { mutableStateOf(true) }

    var showMonthsProgress by remember { mutableStateOf(false) }
    var showCategoriesProgress by remember { mutableStateOf(false) }
    var showCategoriesProgress1 by remember { mutableStateOf(false) }


    // Update local balance and hide progress when balanceState changes
    LaunchedEffect(balanceState.value) {
        if (balance != balanceState.value && balanceState.value >= 0.0) {
            balance = balanceState.value
            showBalanceProgress = false
        }
    }

    // Show error message if update fails
    LaunchedEffect(updateBalanceState.errorMessage) {
        updateBalanceState.errorMessage?.let { error ->
            showBalanceProgress = false
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.resetUpdateState()
        }
    }

    // Handle update success: fetch balance, then reset update state
    LaunchedEffect(updateBalanceState.success) {
        if (updateBalanceState.success) {
            showBalanceProgress = true
            viewModel.getCurrentBalance()
            viewModel.resetUpdateState()
        }
    }


    // Listen to changes with LaunchedEffect when state changes
    LaunchedEffect(monthData) {
        when (monthData) {

            is Resource.Loading -> {
                // Show loading log or trigger something
                showMonthsProgress = true
            }

            is Resource.Error -> {
                val message = (monthData as Resource.Error).message
                println("Error: $message")
                Toast.makeText(context, message ?: "Unknown error", Toast.LENGTH_LONG).show()
                showMonthsProgress = false
            }

            is Resource.Success -> {
                showMonthsProgress = false
            }
        }
    }


    LaunchedEffect(mostUsedCategories) {
        when (mostUsedCategories) {

            is Resource.Loading -> {
                // Show loading log or trigger something
                showCategoriesProgress = true
            }

            is Resource.Error -> {
                val message = (monthData as Resource.Error).message
                println("Error: $message")
                Toast.makeText(
                    context,
                    message ?: "Unknown error loading categories",
                    Toast.LENGTH_LONG
                ).show()
                showCategoriesProgress = false
            }

            is Resource.Success -> {
                showCategoriesProgress = false
            }
        }
    }

    LaunchedEffect(categories) {
        when (categories) {

            is Resource.Loading -> {
                // Show loading log or trigger something
                showCategoriesProgress1 = true
            }

            is Resource.Error -> {
                val message = (monthData as Resource.Error).message
                println("Error: $message")
                Toast.makeText(
                    context,
                    message ?: "Unknown error loading categories",
                    Toast.LENGTH_LONG
                ).show()
                showCategoriesProgress1 = false
            }

            is Resource.Success -> {
                showCategoriesProgress1 = false
            }
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

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Box(
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

                        if (showBalanceProgress) {
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

///////////////////////// To Be Reviewed Later //////////////////////

//            // Daily expenses chart
//            Text(
//                text = stringResource(id = R.string.monthly_expenses),
//                fontSize = 16.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = colorResource(id = R.color.dark_green)
//            )
//
//
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//            )
//            {
//
//
//                if (showMonthsProgress) {
//                    CircularProgressIndicator(
//                        color = colorResource(id = R.color.dark_green)
//                    )
//                } else {
//
//                    if (monthData.data != null) {
//                        MonthlyExpenses(
//                            modifier = Modifier.padding(top = 15.dp),
//                            months = monthData.data!!
//                        )
//                    }
//                }
//            }
//
//
//            Spacer(modifier = Modifier.height(50.dp))

///////////////////////// To Be Reviewed Later //////////////////////

            // Most used categories
            Text(
                text = stringResource(id = R.string.most_used_categories),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.dark_green)
            )




            if (showCategoriesProgress) {

                ShimmerCategoryItem(
                    modifier = Modifier.padding(10.dp),
                    loading = showCategoriesProgress
                ) {

                }

                ShimmerCategoryItem(
                    modifier = Modifier.padding(10.dp),
                    loading = showCategoriesProgress
                ) {

                }

                ShimmerCategoryItem(
                    modifier = Modifier.padding(10.dp),
                    loading = showCategoriesProgress
                ) {

                }

                ShimmerCategoryItem(
                    modifier = Modifier.padding(10.dp),
                    loading = showCategoriesProgress
                ) {

                }

            } else {

                if (mostUsedCategories.data != null) {
                    MostUsedCategories(
                        modifier = Modifier.padding(top = 15.dp),
                        list = mostUsedCategories.data!!
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Expenses categories
            Text(
                text = stringResource(id = R.string.expenses_categories),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.dark_green)
            )


            if (showCategoriesProgress1) {

                ShimmerCategoryItem(
                    modifier = Modifier.padding(10.dp),
                    loading = showCategoriesProgress1
                ) {

                }

                ShimmerCategoryItem(
                    modifier = Modifier.padding(10.dp), loading = showCategoriesProgress1
                ) {

                }

                ShimmerCategoryItem(
                    modifier = Modifier.padding(10.dp), loading = showCategoriesProgress1
                ) {

                }

                ShimmerCategoryItem(
                    modifier = Modifier.padding(10.dp), loading = showCategoriesProgress1
                ) {

                }

                ShimmerCategoryItem(
                    modifier = Modifier.padding(10.dp), loading = showCategoriesProgress1
                ) {

                }

                ShimmerCategoryItem(
                    modifier = Modifier.padding(10.dp), loading = showCategoriesProgress1
                ) {

                }

            } else {

                if (categories.data != null) {
                    Categories(
                        modifier = Modifier.padding(top = 15.dp, bottom = 35.dp),
                        list = categories.data!!
                    )
                }
            }

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
                .padding(
                    bottom = 100.dp,
                    end = 15.dp
                ), // Optional padding to give some space from edges
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

                    showBalanceProgress = true
                    viewModel.updateBalance(amount = amount, type = type, categoryName = category)

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