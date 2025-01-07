package com.ssoaharison.mypasswordmanager.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

data class DetailFilterModel(
    val type: DetailType = DetailType.CREDENTIALS,
    @StringRes val typeName: Int = DetailType.CREDENTIALS.getTypeName(),
    val isSelected: Boolean = true
)

@Composable
fun generateDetailFilterExample() = listOf(
    DetailFilterModel(DetailType.CREDENTIALS, DetailType.CREDENTIALS.getTypeName(), true),
    DetailFilterModel(DetailType.ADDRESS, DetailType.CREDENTIALS.getTypeName(), false),
    DetailFilterModel(DetailType.BIRTHDAY, DetailType.CREDENTIALS.getTypeName(), false),
)