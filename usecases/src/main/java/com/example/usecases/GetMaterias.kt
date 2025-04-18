package com.example.usecases

import com.example.data.MateriaRepository
import com.example.data.NetworkResult
import com.example.domain.Materia

class GetMaterias(val materiaRepository: MateriaRepository) {
    suspend fun invoke(): NetworkResult<List<Materia>> {
        return materiaRepository.getMaterias()
    }
}