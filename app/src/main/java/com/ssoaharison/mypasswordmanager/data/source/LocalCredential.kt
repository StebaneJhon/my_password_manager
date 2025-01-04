package com.ssoaharison.mypasswordmanager.data.source

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credential")
data class LocalCredential (
    @PrimaryKey(autoGenerate = false) val id: String,
    var username: String,
    var password: String,
    var viewCount: Int,
)