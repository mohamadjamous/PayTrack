package com.app.paytrack.view.sign_in

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R
import com.app.paytrack.view.components.AccountsBox
import com.app.paytrack.view.components.CustomButton
import com.app.paytrack.view.components.OnBoardingButton


@Composable
fun CreateAccountScreen(modifier: Modifier = Modifier) {

    var accounts by remember { mutableStateOf(listOf<String>()) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = stringResource(id = R.string.new_account),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = colorResource(id = R.color.dark_green)
        )


        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(id = R.string.new_account_desc),
            fontSize = 18.sp,
            color = colorResource(id = R.color.dark_green)
        )


        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            AccountsBox(
                modifier = Modifier.padding(top = 30.dp),
                accounts = accounts,
                onAddAccount = { newAccount -> accounts = accounts + newAccount },
                onRemoveAccount = { account -> accounts = accounts - account }
            )

            Spacer(modifier = Modifier.weight(1f))

            OnBoardingButton(
                modifier = Modifier.padding(bottom = 30.dp),
                text = stringResource(id = R.string.done)
            ) {

                // Check if user created at least one account
                if (accounts.isEmpty()) {
                    Toast.makeText(context, "Please add at least one account", Toast.LENGTH_LONG)
                        .show()
                } else if (accounts.size > 2) {
                    Toast.makeText(context, "Cannot add more than 3 accounts", Toast.LENGTH_LONG)
                        .show()
                } else {
                    // Save account info
                    // Navigate to main screen
                }


            }
        }


    }

}


@Preview(showSystemUi = true)
@Composable
fun CreateAccountScreenPreview(modifier: Modifier = Modifier) {
    CreateAccountScreen()
}