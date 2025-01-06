package com.ssoaharison.mypasswordmanager.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

data class DetailFilterModel(
    val type: DetailType,
    @StringRes val typeName: Int,
    val isSelected: Boolean
)

@Composable
fun generateDetailFilterExample() = listOf(
    DetailFilterModel(DetailType.CREDENTIALS, DetailType.CREDENTIALS.getTypeName(), true),
    DetailFilterModel(DetailType.ADDRESS, DetailType.CREDENTIALS.getTypeName(), false),
    DetailFilterModel(DetailType.BIRTHDAY, DetailType.CREDENTIALS.getTypeName(), false),
)