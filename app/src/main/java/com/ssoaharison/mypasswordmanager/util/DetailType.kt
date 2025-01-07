package com.ssoaharison.mypasswordmanager.util

import com.ssoaharison.mypasswordmanager.R

enum class DetailType {
    CREDENTIALS,
    ADDRESS,
    BIRTHDAY;

    fun getTypeName() = when (this) {
        CREDENTIALS -> R.string.credentials
        ADDRESS -> R.string.address
        BIRTHDAY -> R.string.birthday
    }

}