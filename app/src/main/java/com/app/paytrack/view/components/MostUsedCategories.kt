package com.app.paytrack.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.paytrack.R
import com.app.paytrack.model.Category

@Composable
fun MostUsedCategories(
    modifier: Modifier = Modifier,
    list: List<Category>
) {

    Column(modifier = modifier) {
        list.take(4).forEach { category ->
            CategoryItem(
                painter = category.painter,
                name = category.name,
                value = category.value
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun MostUsedCategoriesPreview(modifier: Modifier = Modifier) {

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
    MostUsedCategories(list = list)
}