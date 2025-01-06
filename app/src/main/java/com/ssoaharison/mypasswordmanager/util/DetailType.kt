package com.ssoaharison.mypasswordmanager.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ssoaharison.mypasswordmanager.R

enum class DetailType {
    CREDENTIALS,
    ADDRESS,
    BIRTHDAY;

    @Composable
    fun getTypeName() = when (this) {
        CREDENTIALS -> R.string.credentials
        ADDRESS -> R.string.address
        BIRTHDAY -> R.string.birthday
    }



}