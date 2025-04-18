package com.app.paytrack.view.main

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    var data = viewModel.data.collectAsState().value
    var selectedData by remember {
        mutableStateOf(data.expenseData)
    }
    var selectedName by remember {
        mutableStateOf("")
    }
    
    LaunchedEffect(selectedIndex) {
        
        if ( selectedIndex == 0){
            selectedName = "Expenses"
            selectedData = data.expenseData
        }else{
            selectedName = "Income"
            selectedData = data.incomeData
        }
    }

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
    }

}


@Preview(showSystemUi = true)
@Composable
fun ChartsScreenPreview(modifier: Modifier = Modifier) {
    ChartsScreen(
     viewModel = ChartsViewModel()
    )

}