package com.app.paytrack.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    name: String,
    value: String
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(id = R.color.dark_green_1)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {


        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = colorResource(id = R.color.cyan))
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Text(
                text = value,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

        }


    }
}


@Preview(showSystemUi = true)
@Composable
fun CategoryItemPreview(modifier: Modifier = Modifier) {

    CategoryItem(
        painter = painterResource(id = R.drawable.person),
        name = "Lorem",
        value = "7,000€"
    )

}