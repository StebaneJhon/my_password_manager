package com.ssoaharison.mypasswordmanager.data

data class ExternalCredential(
    val id: String,
    val username: String,
    val password: String,
    val viewCount: Int
)