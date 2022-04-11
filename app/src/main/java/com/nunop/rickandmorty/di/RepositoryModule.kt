package com.nunop.rickandmorty.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.nunop.rickandmorty.data.paging.CharacterRemoteMediator
import com.nunop.rickandmorty.data.paging.EpisodeRemoteMediator
import com.nunop.rickandmorty.data.paging.LocationsPagingDataSource
import com.nunop.rickandmorty.data.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.data.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.repository.character.CharacterRepository
import com.nunop.rickandmorty.repository.character.CharacterRepositoryImpl
import com.nunop.rickandmorty.repository.episode.EpisodeRepository
import com.nunop.rickandmorty.repository.episode.EpisodeRepositoryImpl
import com.nunop.rickandmorty.repository.location.LocationRepository
import com.nunop.rickandmorty.repository.location.LocationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
@ExperimentalPagingApi
object RepositoryModule {


    @ViewModelScoped
    @Provides
    fun provideLocationsPagingDataSource(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ) = LocationsPagingDataSource(
        remoteDataSource,
        localDataSource
    )

    @ViewModelScoped
    @Provides
    fun provideEpisodeRemoteMediator(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ) = EpisodeRemoteMediator(
        remoteDataSource,
        localDataSource
    )

    @ViewModelScoped
    @Provides
    fun provideCharacterRemoteMediator(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ) = CharacterRemoteMediator(
        remoteDataSource,
        localDataSource
    )

    @ViewModelScoped
    @Provides
    fun provideCharacterRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        @ApplicationContext context: Context,
        characterRemoteMediator: CharacterRemoteMediator
    ) = CharacterRepositoryImpl(
        remoteDataSource,
        localDataSource,
        context,
        characterRemoteMediator
    ) as CharacterRepository

    @ViewModelScoped
    @Provides
    fun provideEpisodeRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        @ApplicationContext context: Context,
        episodeRemoteMediator: EpisodeRemoteMediator
    ) = EpisodeRepositoryImpl(
        remoteDataSource,
        localDataSource,
        context,
        episodeRemoteMediator
    ) as EpisodeRepository

    @ViewModelScoped
    @Provides
    fun provideLocationRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        @ApplicationContext context: Context,
        locationsPagingDataSource: LocationsPagingDataSource
    ) = LocationRepositoryImpl(
        remoteDataSource,
        localDataSource,
        context,
        locationsPagingDataSource
    ) as LocationRepository
}