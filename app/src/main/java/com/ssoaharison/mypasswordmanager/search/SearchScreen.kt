package com.ssoaharison.mypasswordmanager.search

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.commonUiElements.DetailsList
import com.ssoaharison.mypasswordmanager.commonUiElements.GeneralTopAppBar
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme

@Composable
fun SearchScreen(
    @StringRes userMessage: Int,
    onDetailClicked: (ExternalCredential) -> Unit,
    onAddNewDetail: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            GeneralTopAppBar(
                R.string.search,
                onRefresh,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onAddNewDetail) {
                Icon(Icons.Default.Add, stringResource(R.string.new_detail))
            }
        }
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(paddingValues)
                .fillMaxSize()
        ) {
            MySearchBar(
                viewModel::onSearchQueryChange,
                uiState.searchQuery,
                Modifier.padding(horizontal = dimensionResource(R.dimen.horizontal_margin))
            )
            if(uiState.searchQuery.isNotBlank()) {
                DetailsList(
                    title = stringResource(R.string.credentials,),
                    details = uiState.items,
                    onDetailClicked = onDetailClicked,
                    Modifier.padding(
                        vertical = dimensionResource(R.dimen.horizontal_margin),
                        horizontal = dimensionResource(R.dimen.horizontal_margin),
                    )
                )
            } else {
                Text(text = stringResource(uiState.userMessage ?: R.string.on_no_detail_found), style = MaterialTheme.typography.labelSmall)
            }

            uiState.userMessage?.let { userMessage ->
                val snackbarText = stringResource(userMessage)
                LaunchedEffect(snackbarHostState, viewModel, userMessage, snackbarText) {
                    snackbarHostState.showSnackbar(snackbarText)
                    viewModel.snackbarMessageShown()
                }
            }

        }
    }
}

@Composable
fun MySearchBar(
    onSearchQueryChange: (String) -> Unit,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        leadingIcon = { Icon(imageVector = Icons.Default.Search, null) },
        onValueChange = onSearchQueryChange,
        placeholder = { Text(text = stringResource(R.string.search)) },
        maxLines = 1,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    MyPasswordManagerTheme {
        Surface {
            MySearchBar({}, "")
        }
    }
}