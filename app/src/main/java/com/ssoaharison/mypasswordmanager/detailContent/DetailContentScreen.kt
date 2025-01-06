package com.ssoaharison.mypasswordmanager.detailContent

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ssoaharison.mypasswordmanager.commonUiElements.GenericTopAppBar
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme

@Composable
fun DetailContentScreen(
    topBarTitle: String,
    detail: ExternalCredential,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold (
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            GenericTopAppBar(topBarTitle, onBack)
        }
    ) { paddingValues ->
        DetailContent(
            detail = detail,
            Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun DetailContent(
    detail: ExternalCredential,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.horizontal_margin))
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = detail.appName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.item_padding))
            )
        Text(
            text = detail.link,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.item_padding))
        )
        Text(
            text = detail.username,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.item_padding))
        )
        Text(
            text = detail.password,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.item_padding))
        )
    }
}

@Preview
@Composable
fun DetailContentScreenPreview() {
    MyPasswordManagerTheme {
        Surface {
            DetailContentScreen(
                stringResource(R.string.detail_content, "Credential"),
                ExternalCredential("", "Youtube", "youtube.com", "Banne", "123456", 0),
                {}
            )
        }
    }
}

@Preview
@Composable
fun DetailContentPreview() {
    MyPasswordManagerTheme {
        Surface {
            DetailContent(
                ExternalCredential("", "Youtube", "youtube.com", "Banne", "123456", 0)
            )
        }
    }
}























