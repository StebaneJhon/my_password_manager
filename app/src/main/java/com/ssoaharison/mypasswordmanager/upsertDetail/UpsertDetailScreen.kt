package com.ssoaharison.mypasswordmanager.upsertDetail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.commonUiElements.DetailUpsertTopAppBar
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme

@Composable
fun UpsertDetailScreen(
    @StringRes topBarTitle: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold (
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DetailUpsertTopAppBar(topBarTitle, onBack) {
                //TODO: Save detail
            }
        }
    ){ paddingValues ->
        val appName = ""
        val applink = ""
        val userame = ""
        val password = ""
        UpsertDetailContent(
            appName,
            applink,
            userame,
            password,
            {
                //TODO: Implementing app name change
            },
            {
                //TODO: Implementing app link change
            },
            {
                //TODO: Implementing user name change
            },
            {
                //TODO: Implementing password change
            },
            Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun UpsertDetailContent(
    appName: String,
    link: String,
    username: String,
    password: String,
    onAppNameChange: (String) -> Unit,
    onLinkChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.horizontal_margin))
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = appName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.item_padding)),
            onValueChange = onAppNameChange,
            placeholder = { Text(text = stringResource(R.string.hint_app_or_website_name)) },
            maxLines = 1
        )
        OutlinedTextField(
            value = link,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.item_padding)),
            onValueChange = onLinkChange,
            placeholder = { Text(text = stringResource(R.string.hint_website_link)) },
            maxLines = 1
        )
        OutlinedTextField(
            value = username,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.item_padding)),
            onValueChange = onUsernameChange,
            placeholder = { Text(text = stringResource(R.string.hint_username)) },
            maxLines = 1
        )
        OutlinedTextField(
            value = password,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.item_padding)),
            onValueChange = onPasswordChange,
            placeholder = { Text(text = stringResource(R.string.hint_password)) },
            maxLines = 1,
        )
    }
}

@Preview
@Composable
fun UpsertDetailScreenPreview() {
    MyPasswordManagerTheme {
        Surface {
            UpsertDetailScreen(
                topBarTitle = R.string.add_new_detail,
                {},
            )
        }
    }
}

@Preview
@Composable
fun UpsertDetailContentPreview() {
    MyPasswordManagerTheme {
        Surface {
            UpsertDetailContent(
                "", "", "", "", {}, {}, {}, {}
            )
        }
    }
}





