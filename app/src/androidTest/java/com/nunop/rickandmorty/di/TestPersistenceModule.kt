package com.nunop.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.nunop.rickandmorty.data.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestPersistenceModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, Database::class.java)
            .allowMainThreadQueries()
            .build()
}