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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.commonUiElements.GenericTopAppBar
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme

@Composable
fun DetailContentScreen(
    topBarTitle: String,
    onEditDetail: (String) -> Unit,
    onDeleteDetail: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailContentViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {

    Scaffold (
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            GenericTopAppBar(
                title = topBarTitle,
                onBack = onBack,
                onEdit = { onEditDetail(viewModel.detailId) },
                onDelete = viewModel::deleteDetail
                )
        }
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        DetailContent(
            detail = uiState.detail,
            Modifier.padding(paddingValues)
        )

        uiState.userMessage?.let { userMessage ->
            val snackbarText = stringResource(userMessage)
            LaunchedEffect(snackbarHostState, viewModel, userMessage, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }

        LaunchedEffect(uiState.isDetailDeleted) {
            if (uiState.isDetailDeleted) {
                onDeleteDetail()
            }
        }

    }
}

@Composable
fun DetailContent(
    detail: ExternalCredential?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.horizontal_margin))
            .verticalScroll(rememberScrollState())
    ) {
        detail?.let {
            Text(
                text = it.appName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.item_padding))
            )
            Text(
                text = it.link,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.item_padding))
            )
            Text(
                text = it.username,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.item_padding))
            )
            Text(
                text = it.password,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.item_padding))
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























