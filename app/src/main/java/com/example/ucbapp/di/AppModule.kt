package com.example.ucbapp.di

import android.content.Context
import com.example.data.SubjectRepository
import com.example.data.materia.IMateriaRemoteDataSource
import com.example.framework.service.RetrofitBuilder
import com.example.framework.materia.MateriaRemoteDataSource
import com.example.usecases.GetTeacherSubjects
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
    fun subjectRemoteDataSource(retrofiService: RetrofitBuilder): IMateriaRemoteDataSource {
        return MateriaRemoteDataSource(retrofiService)
    }

    @Provides
    @Singleton
    fun subjectRepository(remoteDataSource: IMateriaRemoteDataSource): SubjectRepository {
        return SubjectRepository(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideGitUseCases(subjectRepository: SubjectRepository): GetTeacherSubjects {
        return GetTeacherSubjects(subjectRepository)
    }
}
