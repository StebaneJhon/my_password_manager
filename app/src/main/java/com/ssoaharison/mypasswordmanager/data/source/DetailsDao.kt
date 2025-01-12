package com.ssoaharison.mypasswordmanager.data.source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailsDao {

    @Query("SELECT * FROM credential")
    fun observeAllCredentials(): Flow<List<LocalCredential>>

    @Query("SELECT * FROM credential WHERE id = :credentialId")
    fun observeCredentialById(credentialId: String): Flow<LocalCredential>

    @Query("SELECT * FROM credential WHERE id LIKE :query OR username LIKE :query OR password LIKE :query OR appName LIKE :query OR link LIKE :query")
    fun observeCredentialsByQuery(query: String): Flow<List<LocalCredential>>

    @Query("SELECT * FROM credential WHERE id LIKE :query OR username LIKE :query OR password LIKE :query")
    suspend fun searchCredentialsByQuery(query: String): List<LocalCredential>

    @Query("SELECT * FROM credential")
    suspend fun getAllCredentials(): List<LocalCredential>

    @Query("SELECT * FROM credential WHERE id = :credentialId")
    fun getCredentialById(credentialId: String): LocalCredential?

    @Query("SELECT * FROM credential WHERE appName = :credentialAppName")
    fun getCredentialByAppName(credentialAppName: String): LocalCredential?

    @Upsert
    suspend fun upsertCredential(credential: LocalCredential)

    @Query("UPDATE credential SET viewCount = :viewCount WHERE id = :credentialId")
    suspend fun updateCredentialViewCount(credentialId: String, viewCount: Int)

    @Query("DELETE FROM credential WHERE id = :credentialId")
    suspend fun deleteCredentialById(credentialId: String)

    @Query("DELETE FROM credential")
    suspend fun deleteAllCredentials()

}