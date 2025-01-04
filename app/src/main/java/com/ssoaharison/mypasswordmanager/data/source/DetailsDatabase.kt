package com.ssoaharison.mypasswordmanager.data.source

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalCredential::class],
    version = 1,
    exportSchema = false
)
abstract class DetailsDatabase: RoomDatabase() {
    abstract fun detailsDao(): DetailsDao
}