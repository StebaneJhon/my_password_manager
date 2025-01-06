package com.ssoaharison.mypasswordmanager.data

import com.ssoaharison.mypasswordmanager.data.source.LocalCredential

fun ExternalCredential.toLocal() = LocalCredential(
    id = id,
    appName = appName,
    link = link,
    username = username,
    password = password,
    viewCount = viewCount,
)
fun List<ExternalCredential>.toLocal() = map(ExternalCredential::toLocal)

fun LocalCredential.toExternal() = ExternalCredential(
    id = id,
    appName = appName,
    link = link,
    username = username,
    password = password,
    viewCount = viewCount,
)
@JvmName("localCredentialToExternal")
fun List<LocalCredential>.toExternal() = map(LocalCredential::toExternal)