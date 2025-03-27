package com.example.usecases

import com.example.data.SubjectRepository
import com.example.domain.Subject

class GetTeacherSubjects(val subjectRepository: SubjectRepository) {
    suspend fun invoke(subjectId: String) : Subject {
        return subjectRepository.findbyId(subjectId)
    }
}