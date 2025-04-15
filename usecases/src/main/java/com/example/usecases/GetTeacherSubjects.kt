package com.example.usecases

import com.example.data.SubjectRepository
import com.example.domain.Materia

class GetTeacherSubjects(val subjectRepository: SubjectRepository) {
    suspend fun invoke(subjectId: String) : Materia {
        return subjectRepository.findbyId(subjectId)
    }
}