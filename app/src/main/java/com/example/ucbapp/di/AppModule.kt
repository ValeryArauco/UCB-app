package com.example.ucbapp.di

import android.content.Context
import com.example.data.MateriaRepository
import com.example.data.materia.IMateriaRemoteDataSource
import com.example.framework.service.RetrofitBuilder
import com.example.framework.materia.MateriaRemoteDataSource
import com.example.usecases.GetMaterias
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun providerRetrofitBuilder(@ApplicationContext context: Context) : RetrofitBuilder {
        return RetrofitBuilder(context)
    }

    @Provides
    @Singleton
    fun materiaRemoteDataSource(retrofiService: RetrofitBuilder): IMateriaRemoteDataSource {
        return MateriaRemoteDataSource(retrofiService)
    }

    @Provides
    @Singleton
    fun materiaRepository(remoteDataSource: IMateriaRemoteDataSource): MateriaRepository {
        return MateriaRepository(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideGetMaterias(subjectRepository: MateriaRepository): GetMaterias {
        return GetMaterias(subjectRepository)
    }
}
