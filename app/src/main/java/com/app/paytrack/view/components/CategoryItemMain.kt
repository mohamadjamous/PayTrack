package com.app.paytrack.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R

@Composable
fun CategoryItemMain(
    modifier: Modifier = Modifier,
    painter: Painter,
    name: String,
    desc: String,
    value: String,
    date: String,
    circleColor: Color = colorResource(id = R.color.dark_green)
) {

    Row(
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth()
            .background(colorResource(id = R.color.dark_green_1)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = circleColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(35.dp),
                    painter = painter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = colorResource(id = R.color.white))
                )
            }


            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {

                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )

//                Text(
//                    text = desc,
//                    color = Color.White
//                )
            }


        }

        Column(
            modifier = Modifier.padding(end = 15.dp)
        ) {

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 20.sp
            )

            Text(modifier = Modifier.padding(top = 2.dp),
                text = date,
                color = Color.White,
                fontSize = 15.sp
            )
        }


    }

}


@Preview(showSystemUi = true)
@Composable
fun CategoryItemMainPreview(modifier: Modifier = Modifier) {
    CategoryItemMain(
        painter = painterResource(id = R.drawable.person),
        name = "Lorem Ipsum",
        desc = "Dum & Simple",
        value = "-18,10€",
        date = "June  7"
    )
}