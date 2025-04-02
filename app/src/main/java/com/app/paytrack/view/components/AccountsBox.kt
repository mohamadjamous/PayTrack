package com.app.paytrack.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.paytrack.R
import com.app.paytrack.model.Account


@Composable
fun AccountsBox(
    modifier: Modifier = Modifier,
    accounts: List<Account>,
    onAddAccount: (Account) -> Unit,
    onRemoveAccount: (Account) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var accountName by remember { mutableStateOf("") }
    var accountBalance by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(color = colorResource(id = R.color.dark_green)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(20.dp),
                text = stringResource(id = R.string.accounts),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Image(
                modifier = Modifier
                    .padding(top = 17.dp, end = 25.dp)
                    .size(30.dp),
                painter = painterResource(id = R.drawable.card_icon),
                contentDescription = null
            )
        }

        Divider(
            modifier = Modifier.padding(bottom = 20.dp),
            thickness = 2.dp,
            color = colorResource(id = R.color.divider)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 20.dp, start = 25.dp)
                .clickable { showDialog = true }
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.plus_icon),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = 30.dp),
                text = stringResource(id = R.string.add),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(id = R.color.cyan)
            )
        }

        LazyColumn {
            items(accounts) { account ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "${account.name} - ${account.balance}",
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    IconButton(onClick = { onRemoveAccount(account) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    stringResource(id = R.string.add_account),
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Column {

                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp),
                        value = accountName,
                        hint = stringResource(id = R.string.account_name)
                    ) { accountName = it }


                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp),
                        value = accountBalance,
                        hint = stringResource(id = R.string.account_balance),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    ) { input ->
                        // Allow only numeric input
                        if (input.all { it.isDigit() }) {
                            accountBalance = input
                        }
                    }

                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (accountName.isNotBlank() && accountBalance.isNotBlank()) {
                        onAddAccount(Account(accountName, accountBalance.toDouble()))
                        accountName = ""
                        accountBalance = ""
                        showDialog = false
                    }
                }) {
                    Text(stringResource(id = R.string.add))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun AccountsBoxPreview(modifier: Modifier = Modifier) {
    AccountsBox(accounts = listOf(Account("", 0.0)), onAddAccount = {

    }) {

    }
}