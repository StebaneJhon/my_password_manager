package com.ssoaharison.mypasswordmanager.insight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssoaharison.mypasswordmanager.R
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

        Column (
            modifier = Modifier.padding(paddingValue).fillMaxSize()
        ) {
            DetailsList(
                stringResource(R.string.strong_passwords),
                uiState.strongPasswords,
                onItemClicked,
                Modifier.padding(vertical = dimensionResource(R.dimen.horizontal_margin))
            )
            DetailsList(
                stringResource(R.string.weak_passwords),
                uiState.weakPasswords,
                onItemClicked
            )
            DetailsList(
                stringResource(R.string.duplicated_passwords),
                uiState.duplicatedPasswords,
                onItemClicked,
                Modifier.padding(vertical = dimensionResource(R.dimen.horizontal_margin))
            )
        }

    }
}