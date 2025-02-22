package com.ssoaharison.mypasswordmanager.data

import com.ssoaharison.mypasswordmanager.data.source.DetailsDao
import com.ssoaharison.mypasswordmanager.data.source.LocalCredential
import com.ssoaharison.mypasswordmanager.di.DefaultDispatcher
import com.ssoaharison.mypasswordmanager.util.isPasswordWeak
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultDetailsRepository @Inject constructor(
    private val dataSource: DetailsDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : DetailsRepository {
    override fun getCredentialsStream(): Flow<List<ExternalCredential>> {
        return dataSource.observeAllCredentials().map { credentials ->
            withContext(dispatcher) {
                credentials.toExternal()
            }
        }
    }

    override fun getCredentialsWithWeakPasswordStream(): Flow<List<ExternalCredential>> {
        return dataSource.observeAllCredentials().map { credentials ->
            withContext(dispatcher) {
                credentials.filter { isPasswordWeak(it.password) }.toExternal()
            }
        }
    }

    override fun getCredentialsWithStrongPasswordStream(): Flow<List<ExternalCredential>> {
        return dataSource.observeAllCredentials().map { credentials ->
            withContext(dispatcher) {
                credentials.filter { !isPasswordWeak(it.password) }.toExternal()
            }
        }
    }

    override fun getCredentialsWithDuplicatedPasswordStream(): Flow<List<ExternalCredential>> {
        return dataSource.observeAllCredentials().map { credentials ->
            withContext(dispatcher) {
                val result = arrayListOf<LocalCredential>()
                for (index in 0..credentials.size.minus(1)) {
                    val credentialWithSamePassword = credentials.filter { credentials[index].password == it.password }
                    if (credentialWithSamePassword.size > 1) {
                        credentialWithSamePassword.forEach {
                            if (it !in result) {
                                result.add(it)
                            }
                        }
                    }
                }
                result.toExternal()
            }
        }
    }

    override suspend fun getCredentials(): List<ExternalCredential> {
        return withContext(dispatcher) {
            dataSource.getAllCredentials().toExternal()
        }
    }

    override fun getCredentialStream(credentialId: String): Flow<ExternalCredential> {
        return dataSource.observeCredentialById(credentialId).map { it.toExternal() }
    }

    override suspend fun getCredential(credentialId: String): ExternalCredential? {
        return withContext(dispatcher) {
            dataSource.getCredentialById(credentialId)?.toExternal()
        }
    }

    override suspend fun getCredentialByAppName(appName: String): ExternalCredential? {
        return withContext(dispatcher) {
            dataSource.getCredentialByAppName(appName)?.toExternal()
        }
    }

    override fun observeCredentialsByQuery(query: String): Flow<List<ExternalCredential>> {
        return dataSource.observeCredentialsByQuery(query).map { credentials ->
            withContext(dispatcher) {
                credentials.toExternal()
            }
        }
    }

    override suspend fun searchCredentialsByQuery(query: String): List<ExternalCredential> {
        return withContext(dispatcher) {
            dataSource.searchCredentialsByQuery(query).map { it.toExternal() }
        }
    }

    override suspend fun createCredential(
        appName: String,
        link: String,
        username: String,
        password: String
    ): String {
        val credentialId = now()
        val credential = ExternalCredential(
            id = credentialId,
            appName = appName,
            link = link,
            username = username,
            password = password,
            viewCount = 0
        )
        dataSource.upsertCredential(credential.toLocal())
        return credentialId
    }

    override suspend fun updateCredential(
        credentialId: String,
        appName: String,
        link: String,
        username: String,
        password: String,
        viewCount: Int
    ) {
        val credential = getCredential(credentialId)?.copy(
            appName = appName,
            link = link,
            username = username,
            password = password,
            viewCount = viewCount
        ) ?: throw Exception("Credential (id $credentialId) not found")
        dataSource.upsertCredential(credential.toLocal())
    }

    override suspend fun updateCredentialViewCount(credentialId: String, viewCount: Int) {
        dataSource.updateCredentialViewCount(credentialId, viewCount)
    }

    override suspend fun deleteCredential(credentialId: String) {
        dataSource.deleteCredentialById(credentialId)
    }

    override suspend fun deleteAllCredentials() {
        dataSource.deleteAllCredentials()
    }
}

private fun now(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")
    return LocalDateTime.now().format(formatter)
}