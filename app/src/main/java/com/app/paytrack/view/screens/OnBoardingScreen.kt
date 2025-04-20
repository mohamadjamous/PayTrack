package com.app.paytrack.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.paytrack.R
import com.app.paytrack.model.OnBoardingItem
import com.app.paytrack.model.Screen
import com.app.paytrack.view.components.BackButton
import com.app.paytrack.view.components.OnBoardingButton
import com.app.paytrack.view.components.OnBoardingButtonPreview
import java.util.ArrayList

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val list = remember {
        listOf(
            OnBoardingItem(
                painter = R.drawable.icon_5,
                title = R.string.payment_insights,
                type = 0
            ),
            OnBoardingItem(
                painter = R.drawable.icon_4,
                title = R.string.simple_reports,
                type = 1
            ),
            OnBoardingItem(
                painter = R.drawable.icon_3,
                title = R.string.easy_tracking,
                type = 1
            ),
            OnBoardingItem(
                painter = R.drawable.icon_2,
                title = R.string.full_control,
                type = 0
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .padding(10.dp)
    ) {
        

        Text(
            modifier = Modifier.padding(start = 15.dp, top = 15.dp),
            text = stringResource(id = R.string.benefits_paytrack),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = colorResource(id = R.color.dark_green)
        )

        Text(
            modifier = Modifier
                .padding(start = 15.dp)
                .padding(top = 5.dp),
            text = stringResource(id = R.string.benefits_paytrack_desc),
            fontSize = 18.sp,
            color = colorResource(id = R.color.dark_green)
        )


        LazyVerticalGrid(
            modifier = Modifier.padding(top = 30.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(list) { item ->
                Column(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(
                            color = if (item.type == 0) {
                                colorResource(id = R.color.green)
                            } else {
                                colorResource(id = R.color.dark_green)
                            }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .padding(top = 25.dp)
                            .size(20.dp),
                        painter = painterResource(id = item.painter),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        modifier = Modifier.padding(bottom = 25.dp),
                        text = stringResource(id = item.title),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OnBoardingButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            text = stringResource(id = R.string.get_started)
        ) {
            // Navigate to SignIn screen
            navController.navigate(Screen.SignIn)
        }


    }
}


@Preview(showSystemUi = true)
@Composable
fun OnBoardingScreenPreview(modifier: Modifier = Modifier) {

    OnBoardingScreen(
        navController = rememberNavController()
    )
}