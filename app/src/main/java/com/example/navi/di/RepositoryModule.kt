package com.example.navi.di

import com.example.navi.data.repository.LoginRepositoryImpl
import com.example.navi.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLoginRepository(repository: LoginRepositoryImpl): LoginRepository

}