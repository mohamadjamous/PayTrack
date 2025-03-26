package com.app.paytrack.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.paytrack.R
import com.app.paytrack.view.components.CustomButton
import com.app.paytrack.view.components.CustomTextField
import java.util.Locale


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavController) {


    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {


        Text(
            text = stringResource(id = R.string.sign_in),
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp,
            color = colorResource(id = R.color.dark_green)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                hint = stringResource(id = R.string.email),
                leadingIcon = Icons.Outlined.Email
            ) {
                email = it
            }

            Spacer(modifier = Modifier.height(35.dp))

            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                hint = stringResource(id = R.string.password),
                leadingIcon = Icons.Outlined.Lock,
                passwordVisible = true
            ) {
                password = it
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            textAlign = TextAlign.End,
            text = stringResource(id = R.string.forgot_password),
            color = colorResource(id = R.color.dark_green),
            fontWeight = FontWeight.Bold
        )


        Spacer(modifier = Modifier.height(40.dp))

        CustomButton(
            text = stringResource(id = R.string.sign_in).toUpperCase(Locale.ROOT)) {
            
        }

        OrDivider(
            modifier = Modifier.padding(top = 35.dp)
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier
                    .padding(top = 10.dp),
                text = stringResource(id = R.string.sign_in_with),
                color = colorResource(id = R.color.gray),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            
            Spacer(modifier = Modifier.height(25.dp))

            Image(
                modifier = Modifier.size(35.dp),
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = null
            )

            Row(
                modifier = Modifier.padding(top = 20.dp)
            ){
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    text = stringResource(id = R.string.dont_have_account),
                    color = colorResource(id = R.color.gray),
                    fontSize = 17.sp
                )

                Text(
                    modifier = Modifier
                        .padding(top = 10.dp, start  = 5.dp),
                    text = stringResource(id = R.string.sign_up),
                    color = colorResource(id = R.color.gray),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }

        }


    }

}

@Composable
fun OrDivider(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
    ) {

        Divider(modifier = Modifier.weight(1f), color = Color.Gray, thickness = 2.dp)

        Text(
            text = "OR",
            color = colorResource(id = R.color.dark_green),
            modifier = Modifier.padding(horizontal = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp
        )
        Divider(modifier = Modifier.weight(1f), color = Color.Gray, thickness = 2.dp)
    }
}



@Preview(showSystemUi = true)
@Composable
fun SignInScreenPreview(modifier: Modifier = Modifier) {
    SignInScreen(navController = rememberNavController())
}