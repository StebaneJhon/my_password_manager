package com.ssoaharison.mypasswordmanager.data

data class ExternalCredential(
    val id: String,
    val appName: String,
    val link: String,
    val username: String,
    val password: String,
    val viewCount: Int
)