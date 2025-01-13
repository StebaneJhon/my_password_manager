package com.ssoaharison.mypasswordmanager.detailContent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.commonUiElements.DetailContentTopAppBar
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme
import com.ssoaharison.mypasswordmanager.util.ContentCopyModel

@Composable
fun DetailContentScreen(
    topBarTitle: String,
    onEditDetail: (String) -> Unit,
    onDeleteDetail: () -> Unit,
    onBack: () -> Unit,
    onCopyItemContent: (ContentCopyModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailContentViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DetailContentTopAppBar(
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
            onCopyItemContent = onCopyItemContent,
            modifier = Modifier.padding(paddingValues)
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
    onCopyItemContent: (ContentCopyModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.horizontal_margin))
            .verticalScroll(rememberScrollState())
    ) {
        detail?.let { d ->
            DetailItem(
                stringResource(R.string.label_name),
                d.appName
            ) {
                onCopyItemContent(
                    ContentCopyModel(R.string.label_name, d.appName)
                )
            }
            DetailItem(
                stringResource(R.string.label_link),
                d.link
            ) {
                onCopyItemContent(
                    ContentCopyModel(R.string.label_link, d.link)
                )
            }
            DetailItem(
                stringResource(R.string.label_user_name),
                d.username
            ) {
                onCopyItemContent(
                    ContentCopyModel(R.string.label_user_name, d.username)
                )
            }
            DetailItem(
                stringResource(R.string.label_password),
                d.password,
            ) {
                onCopyItemContent(
                    ContentCopyModel(R.string.label_password, d.password,)
                )
            }
        }
    }
}

@Composable
fun DetailItem(
    title: String,
    content: String,
    onCopyItemContent: () -> Unit
) {
    Column {
        ListItem(
            headlineContent = { Text(title) },
            supportingContent = { Text(content) },
            trailingContent = {
                IconButton(onCopyItemContent) {
                    Icon(
                        painter = painterResource(R.drawable.ic_content_copy),
                        contentDescription = stringResource(R.string.copy, title)
                    )
                }
            }
        )
        HorizontalDivider()
    }
}

@Preview
@Composable
fun DetailContentPreview() {
    MyPasswordManagerTheme {
        Surface {
            DetailContent(
                ExternalCredential("", "Youtube", "youtube.com", "Banne", "123456", 0),
                {}
            )
        }
    }
}

@Composable
@Preview
fun DetailItemPreview() {
    MyPasswordManagerTheme {
        Surface {
            DetailItem("Name", "Content"){}
        }
    }
}























