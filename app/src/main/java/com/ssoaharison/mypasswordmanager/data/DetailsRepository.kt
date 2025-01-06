package com.ssoaharison.mypasswordmanager.data

import kotlinx.coroutines.flow.Flow

interface DetailsRepository {

    fun getCredentialsStream(): Flow<List<ExternalCredential>>

    suspend fun getCredentials(): List<ExternalCredential>

    fun getCredentialStream(credentialId: String): Flow<ExternalCredential>

    suspend fun getCredential(credentialId: String): ExternalCredential?

    fun observeCredentialsByQuery(query: String): Flow<List<ExternalCredential>>

    suspend fun searchCredentialsByQuery(query: String): List<ExternalCredential>

    suspend fun createCredential(appName: String, link: String, username: String, password: String): String

    suspend fun updateCredential(credentialId: String, appName: String, link: String, username: String, password: String, viewCount: Int)

    suspend fun updateCredentialViewCount(credentialId: String, viewCount: Int)

    suspend fun deleteCredential(credentialId: String)

    suspend fun deleteAllCredentials()

}