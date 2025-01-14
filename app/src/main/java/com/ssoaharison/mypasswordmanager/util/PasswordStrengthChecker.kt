package com.ssoaharison.mypasswordmanager.util

import java.util.regex.Pattern


const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{12,}$"

fun isPasswordWeak(password: String): Boolean {
    return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()
}

