package com.nunop.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        Database::class.java,
        "ricky_db"
    ).build()

    @Singleton
    @Provides
    fun provideLocalDataSource(db: Database) = LocalDataSource(db)
}