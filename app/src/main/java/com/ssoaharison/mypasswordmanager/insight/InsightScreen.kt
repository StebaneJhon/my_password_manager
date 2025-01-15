package com.ssoaharison.mypasswordmanager.insight

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.commonUiElements.DetailItem
import com.ssoaharison.mypasswordmanager.commonUiElements.DetailsList
import com.ssoaharison.mypasswordmanager.commonUiElements.GeneralTopAppBar
import com.ssoaharison.mypasswordmanager.data.ExternalCredential

@Composable
fun InsightScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onItemClicked: (ExternalCredential) -> Unit,
    viewModel: InsightViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            GeneralTopAppBar(
                R.string.insight,
                onRefresh
            )
        }

    ) { paddingValue ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LazyColumn(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize(),
            contentPadding = PaddingValues(dimensionResource(R.dimen.content_padding))
        ) {

            item {
                Text(
                    text = stringResource(R.string.strong_passwords)
                )
            }
            items(uiState.strongPasswords) { detail ->
                DetailItem(detail, onItemClicked)
            }

            item {
                Text(
                    text = stringResource(R.string.weak_passwords),
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.vertical_margin))
                )
            }
            items(uiState.weakPasswords) { detail ->
                DetailItem(detail, onItemClicked)
            }

            item {
                Text(
                    text = stringResource(R.string.duplicated_passwords),
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.vertical_margin))
                )
            }
            items(uiState.duplicatedPasswords) { detail ->
                DetailItem(detail, onItemClicked)
            }

        }

    }
}