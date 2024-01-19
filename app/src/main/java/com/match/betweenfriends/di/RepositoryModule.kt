package com.match.betweenfriends.di

import com.match.betweenfriends.data.repository.MainRepositoryImpl
import com.match.betweenfriends.domain.datasource.LocalDataSource
import com.match.betweenfriends.domain.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(
        localDataSource: LocalDataSource
    ): MainRepository = MainRepositoryImpl(localDataSource)

}