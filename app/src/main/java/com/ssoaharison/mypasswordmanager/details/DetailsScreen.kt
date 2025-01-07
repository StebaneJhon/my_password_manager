package com.ssoaharison.mypasswordmanager.details

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.commonUiElements.DetailsList
import com.ssoaharison.mypasswordmanager.commonUiElements.DetailsTopAppBar
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme
import com.ssoaharison.mypasswordmanager.util.DetailFilterModel
import com.ssoaharison.mypasswordmanager.util.generateDetailFilterExample

@Composable
fun DetailsScreen(
    @StringRes userMessage: Int,
    detailsList: List<ExternalCredential>,
    onDetailClicked: (ExternalCredential) -> Unit,
    onAddNewDetail: () -> Unit,
    onToSettings: () -> Unit,
    onRefresh: () -> Unit,
    onFilterChange: (DetailFilterModel) -> Unit,
    onUserMessageDisplayed: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold (
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DetailsTopAppBar(
                R.string.details,
                onRefresh,
                onToSettings
            )
        },
        floatingActionButton = {
            FloatingActionButton(onAddNewDetail) {
                Icon(Icons.Default.Add, stringResource(R.string.new_detail))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            FilterChipItems(
                R.string.filter,
                generateDetailFilterExample(),
                onFilterChange
            )
            DetailsList(
                title = stringResource(R.string.credentials,),
                details = detailsList,
                onDetailClicked = onDetailClicked,
            )
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
    Column(modifier
        .fillMaxWidth()
        .padding(all = dimensionResource(R.dimen.horizontal_margin))
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
fun DetailsScreenPreview() {
    MyPasswordManagerTheme {
        Surface {
            DetailsScreen(
                userMessage = 0,
                detailsList = listOf(
                    ExternalCredential(
                        "111",
                        "Youtube",
                        "aaa",
                        "Banne",
                        "123456",
                        0
                    ),
                    ExternalCredential(
                        "111",
                        "Insta",
                        "aaa",
                        "Banne",
                        "123456",
                        0
                    ),
                    ExternalCredential(
                        "111",
                        "Facebook",
                        "aaa",
                        "Banne",
                        "123456",
                        0
                    )
                ),
                onDetailClicked = {},
                onAddNewDetail = {},
                onToSettings = {},
                onRefresh = {},
                onFilterChange = {},
                onUserMessageDisplayed = {}
            )
        }
    }
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