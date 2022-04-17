package com.nunop.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.datasource.localdatasource.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        Database::class.java,
        "ricky_db"
    ).build()

    @Provides
    @Singleton
    fun provideLocalDataSource(db: Database) = LocalDataSource(db)
}