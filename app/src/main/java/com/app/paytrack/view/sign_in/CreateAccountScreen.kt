package com.app.paytrack.view.sign_in

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.paytrack.R
import com.app.paytrack.model.Account
import com.app.paytrack.model.AccountState
import com.app.paytrack.model.Graph
import com.app.paytrack.view.components.AccountsBox
import com.app.paytrack.view.components.CustomButton
import com.app.paytrack.view.components.CustomDialog
import com.app.paytrack.view.components.OnBoardingButton
import com.app.paytrack.viewmodel.CreateAccountViewModel


@Composable
fun CreateAccountScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateAccountViewModel,
    state: AccountState,
    onAccountCreate: () -> Unit
) {

    var accounts by remember { mutableStateOf(listOf<Account>()) }
    val context = LocalContext.current

    var showDialog by remember {
        mutableStateOf(false)
    }


    // Handle errors from sign-in state
    LaunchedEffect(state.createAccountError) {

        showDialog = false
        state.createAccountError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
        viewModel.resetState()
    }

    // Handle sign-in success for both Google and Email/Password
    LaunchedEffect(state.isCreateAccountSuccess) {


        showDialog = false
        if (state.isCreateAccountSuccess){
           onAccountCreate()
        }

        viewModel.resetState()
    }


    Box(modifier = Modifier.fillMaxSize()) {

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
                        Toast.makeText(
                            context,
                            "Please add at least one account",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else if (accounts.size > 2) {
                        Toast.makeText(
                            context,
                            "Cannot add more than 2 accounts",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else {

                        showDialog = true
                        viewModel.updateUserAccount(list = accounts)
                    }


                }
            }


        }

        CustomDialog(show = showDialog)
    }

}


@Preview(showSystemUi = true)
@Composable
fun CreateAccountScreenPreview(modifier: Modifier = Modifier) {
    CreateAccountScreen(
        viewModel = CreateAccountViewModel(),
        state = AccountState(),
        onAccountCreate = {}
    )
}