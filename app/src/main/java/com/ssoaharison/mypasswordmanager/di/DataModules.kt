package com.ssoaharison.mypasswordmanager.di

import android.content.Context
import androidx.room.Room
import com.ssoaharison.mypasswordmanager.data.DefaultDetailsRepository
import com.ssoaharison.mypasswordmanager.data.DetailsRepository
import com.ssoaharison.mypasswordmanager.data.source.DetailsDao
import com.ssoaharison.mypasswordmanager.data.source.DetailsDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindDetailsRepository(repository: DefaultDetailsRepository): DetailsRepository

}

@Module
@InstallIn(SingletonComponent::class)
object DataModules {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): DetailsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DetailsDatabase::class.java,
            "Details.db"
        ).build()
    }

    @Provides
    fun provideDetailsDao(database: DetailsDatabase): DetailsDao = database.detailsDao()
}