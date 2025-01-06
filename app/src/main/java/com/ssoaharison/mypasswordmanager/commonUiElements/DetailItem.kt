package com.ssoaharison.mypasswordmanager.commonUiElements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ssoaharison.mypasswordmanager.R
import com.ssoaharison.mypasswordmanager.data.ExternalCredential
import com.ssoaharison.mypasswordmanager.ui.theme.MyPasswordManagerTheme

@Composable
fun DetailsList(
    title: String,
    details: List<ExternalCredential>,
    onDetailClicked: (ExternalCredential) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.item_padding),
                vertical = dimensionResource(id = R.dimen.vertical_margin)
            ),
            style = MaterialTheme.typography.headlineSmall
        )
        LazyColumn {
            items(details) { detail ->
                DetailItem(
                    detail,
                    onDetailClicked
                )
            }
        }
    }
}

@Composable
fun DetailItem(
    credential: ExternalCredential,
    onDetailClicked: (ExternalCredential) -> Unit,
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetailClicked(credential) }
    ) {

        ListItem(
            headlineContent = {
                Text(text = credential.appName, style = MaterialTheme.typography.headlineSmall)
            },
            leadingContent = {},
            trailingContent = {
                IconButton(onClick = { onDetailClicked(credential) }) {
                    Icon(
                        painterResource(R.drawable.ic_arrow_forward),
                        contentDescription = stringResource(R.string.details_content)
                    )
                }
            }
        )
        HorizontalDivider()
    }

}

@Preview
@Composable
fun DetailsListPreview() {
    MyPasswordManagerTheme {
        Surface {
            DetailsList(
                title = "Credentials",
                details = listOf(
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
                onDetailClicked = {}
            )
        }
    }
}

@Preview
@Composable
fun DetailItemPreview() {
    MyPasswordManagerTheme {
        Surface {
            DetailItem(
                ExternalCredential(
                    "111",
                    "Youtube",
                    "aaa",
                    "Banne",
                    "123456",
                    0
                ),
            ) { }
        }
    }
}