package com.ssoaharison.mypasswordmanager.details

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.commonUiElements.DetailItem
import com.ssoaharison.mypasswordmanager.commonUiElements.DetailsList
import com.ssoaharison.mypasswordmanager.commonUiElements.GeneralTopAppBar
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme
import com.ssoaharison.mypasswordmanager.util.DetailFilterModel
import com.ssoaharison.mypasswordmanager.util.generateDetailFilterExample

@Composable
fun DetailsScreen(
    @StringRes userMessage: Int,
    onDetailClicked: (ExternalCredential) -> Unit,
    onAddNewDetail: () -> Unit,
    onRefresh: () -> Unit,
    onUserMessageDisplayed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold (
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            GeneralTopAppBar(
                R.string.details,
                onRefresh
            )
        },
        floatingActionButton = {
            FloatingActionButton(onAddNewDetail) {
                Icon(Icons.Default.Add, stringResource(R.string.new_detail))
            }
        }
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(dimensionResource(R.dimen.content_padding))
        ) {
            item{
                FilterChipItems(
                    R.string.filter,
                    uiState.filteringUiInfo,
                    { viewModel.setFiltering(it.type) }
                )
            }
            item {
                Text(
                    text = stringResource(R.string.credentials),
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.vertical_margin))
                )
            }
            items(uiState.items) {detail ->
                DetailItem(detail, onDetailClicked)
            }
        }

        uiState.userMessage?.let { userMessage ->
            val snackbarText = stringResource(userMessage)
            LaunchedEffect(snackbarHostState, viewModel, userMessage, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }

        val currentOnUserMessageDisplayed by rememberUpdatedState(onUserMessageDisplayed)
        LaunchedEffect(userMessage) {
            if ( userMessage != 0) {
                viewModel.showUpsertResultMessage(userMessage)
                currentOnUserMessageDisplayed()
            }
        }

    }
}

@Composable
fun FilterChipItems(
    @StringRes text: Int,
    filters: List<DetailFilterModel>,
    onFilterClicked: (DetailFilterModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.horizontal_margin))
    ) {
        Text(stringResource(text))
        LazyRow (
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.item_padding)),
            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.horizontal_margin)),
            modifier = modifier
        ) {
            items(filters) {filter ->
                FilterChipItem(
                    text = stringResource(filter.typeName),
                    onSelected = { onFilterClicked(filter) },
                    isSelected = filter.isSelected
                )
            }
        }
    }
}

@Composable
fun FilterChipItem(
    text: String,
    onSelected: () -> Unit,
    isSelected: Boolean
) {
    FilterChip(
        onClick = onSelected,
        label = {
            Text(text)
        },
        selected = isSelected,
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Preview
@Composable
fun FilterChipItemsPreview() {
    MyPasswordManagerTheme {
        Surface {
            FilterChipItems(
                R.string.filter,
                generateDetailFilterExample(),
                { }
            )
        }
    }
}

@Preview
@Composable
fun FilterChipItemSelectedPreview() {
    MyPasswordManagerTheme {
        Surface {
            FilterChipItem(
                "Credential",
                {},
                true
            )
        }
    }
}

@Preview
@Composable
fun FilterChipItemUnSelectedPreview() {
    MyPasswordManagerTheme {
        Surface {
            FilterChipItem(
                "Credential",
                {},
                false
            )
        }
    }
}