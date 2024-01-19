package com.match.betweenfriends.di

import com.match.betweenfriends.data.database.PlayerDao
import com.match.betweenfriends.data.source.LocalDataSourceImpl
import com.match.betweenfriends.domain.datasource.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(
        playerDao: PlayerDao,
    ): LocalDataSource = LocalDataSourceImpl(playerDao)

}