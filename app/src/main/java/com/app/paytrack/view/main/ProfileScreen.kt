package com.app.paytrack.view.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.paytrack.R
import com.app.paytrack.view.components.CustomButton
import com.app.paytrack.view.components.CustomDialog
import com.app.paytrack.view.components.CustomTextField
import com.app.paytrack.view.components.SimpleDialog
import com.app.paytrack.viewmodel.ProfileViewModel
import java.util.Locale


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    onSignOutClick: () -> Unit,
) {


    val state by viewModel.state.collectAsState()

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var showDialog by remember {
        mutableStateOf(true)
    }

    var showDeleteAccountDialog by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(state.success) {

        if (state.success) {
            name = state.user?.name.toString()
            email = state.user?.email.toString()
        }

        showDialog = false
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = colorResource(id = R.color.dark_green),
                        shape = CircleShape
                    )
                    .border(
                        border = BorderStroke(
                            20.dp,
                            color = colorResource(id = R.color.dark_green)
                        ),
                        shape = CircleShape
                    )
                    .padding(20.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.coin_iccon),
                    contentDescription = null
                )
            }


            Spacer(modifier = Modifier.padding(top = 40.dp))

            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                hint = stringResource(id = R.string.name),
                leadingIcon = Icons.Outlined.Person,
                enabled = false
            ) {

            }

            Spacer(modifier = Modifier.height(30.dp))

            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                hint = stringResource(id = R.string.email),
                leadingIcon = Icons.Outlined.Email,
                passwordVisible = false,
                enabled = false
            ) {

            }


            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                text = stringResource(id = R.string.log_out)
            ) {
                onSignOutClick()
            }

            Text(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 90.dp)
                    .clickable {
                        showDeleteAccountDialog = true
                    },
                text = stringResource(id = R.string.delete_account),
                color = Color.Red
            )
        }

        CustomDialog(show = showDialog)

        if (showDeleteAccountDialog) {
            SimpleDialog(
                onConfirm = {
                    viewModel.deleteAccount()
//                    showDialog = true
                },
                onCancel = {
                    showDeleteAccountDialog = false
                }
            )
        }
    }

}


@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview(modifier: Modifier = Modifier) {
    ProfileScreen(
        viewModel = ProfileViewModel()
    ) {

    }
}