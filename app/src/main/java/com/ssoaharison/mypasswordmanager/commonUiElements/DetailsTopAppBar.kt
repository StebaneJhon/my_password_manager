package com.ssoaharison.mypasswordmanager.commonUiElements

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopAppBar(
    @StringRes title: Int,
    onIcon: () -> Unit,
    onToSettings: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(title)) },
        navigationIcon = {
            IconButton(onClick = onIcon) {
                Icon(painter = painterResource(R.drawable.ic_box), contentDescription =  stringResource(R.string.app_icon))
            }
        },
        actions = {
            IconButton(onClick = onToSettings) {
                Icon(painter = painterResource(R.drawable.ic_settings), contentDescription = stringResource(R.string.menu_to_settings))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailUpsertTopAppBar(
    @StringRes title: Int,
    onExit: () -> Unit,
    onSave: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(title)) },
        navigationIcon = {
            IconButton(onClick = onExit) {
                Icon(imageVector = Icons.Default.Close, contentDescription =  stringResource(R.string.app_icon))
            }
        },
        actions = {
            TextButton(onClick = onSave) {
                Text(text = stringResource(R.string.save))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericTopAppBar(
    title: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription =  stringResource(R.string.app_icon))
            }
        },
        actions = {
            IconButton(onClick = onEdit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.menu_edit))
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.menu_delete))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun DetailsTopAppBarPreview() {
    MyPasswordManagerTheme {
        Surface {
            DetailsTopAppBar(R.string.search, {}) {}
        }
    }
}

@Preview
@Composable
private fun DetailUpsertTopAppBarPreview() {
    MyPasswordManagerTheme {
        Surface {
            DetailUpsertTopAppBar(R.string.add_new_detail, {}) {}
        }
    }
}

@Preview
@Composable
private fun GenericTopAppBarTopAppBarPreview() {
    MyPasswordManagerTheme {
        Surface {
            GenericTopAppBar(stringResource(R.string.detail_content, "Credential"), {}, {}) {}
        }
    }
}