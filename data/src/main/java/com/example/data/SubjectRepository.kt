package com.example.data

import com.example.data.subject.ISubjectRemoteDataSource
import com.example.domain.Subject

class SubjectRepository (private val remoteDataSource: ISubjectRemoteDataSource){
    suspend fun findbyId(subjectId: String): Subject{
        return this.remoteDataSource.fetch(subjectId)
    }
}
