package com.app.paytrack.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.paytrack.R
import com.app.paytrack.model.Category

@Composable
fun Categories(
    modifier: Modifier = Modifier,
    list: List<Category>
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(id = R.color.dark_green_1)),
    ) {

        list.take(6).forEach {
            CategoryItemMain(
                painter = it.painter,
                name = it.name,
                desc = it.desc,
                value = it.value,
                date = it.date
            )

            Divider()
        }
    }


}


@Preview(showSystemUi = true)
@Composable
fun CategoriesPreview(modifier: Modifier = Modifier) {

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
    Categories(list = list)
}