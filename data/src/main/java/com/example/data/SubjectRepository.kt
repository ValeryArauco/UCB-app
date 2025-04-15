package com.example.data

import com.example.data.materia.IMateriaRemoteDataSource
import com.example.domain.Materia

class SubjectRepository (private val remoteDataSource: IMateriaRemoteDataSource){
    suspend fun findbyId(subjectId: String): Materia{
        return this.remoteDataSource.fetch(subjectId)
    }
}
