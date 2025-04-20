package com.example.ucbapp.di

import android.content.Context
import com.example.data.MateriaRepository
import com.example.data.materia.IMateriaRemoteDataSource
import com.example.framework.materia.MateriaRemoteDataSource
import com.example.framework.service.RetrofitBuilder
import com.example.usecases.DoLogin
import com.example.usecases.GetMaterias
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providerRetrofitBuilder(
        @ApplicationContext context: Context,
    ): RetrofitBuilder = RetrofitBuilder(context)

    @Provides
    @Singleton
    fun materiaRemoteDataSource(retrofiService: RetrofitBuilder): IMateriaRemoteDataSource = MateriaRemoteDataSource(retrofiService)

    @Provides
    @Singleton
    fun materiaRepository(remoteDataSource: IMateriaRemoteDataSource): MateriaRepository = MateriaRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideGetMaterias(materiaRepository: MateriaRepository): GetMaterias = GetMaterias(materiaRepository)

    @Provides
    @Singleton
    fun provideLogin(materiaRepository: MateriaRepository): DoLogin = DoLogin(materiaRepository)
}
