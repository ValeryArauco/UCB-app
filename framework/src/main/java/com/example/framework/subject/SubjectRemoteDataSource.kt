package com.example.framework.subject

import com.example.data.subject.ISubjectRemoteDataSource
import com.example.domain.Subject
import com.example.framework.mappers.toModel
import com.example.framework.service.RetrofitBuilder

class SubjectRemoteDataSource(val retrofitService: RetrofitBuilder): ISubjectRemoteDataSource{
    override suspend fun fetch(subjectId: String): Subject{
        return retrofitService.apiService.getInfoMateria(subjectId).toModel()
    }
}

