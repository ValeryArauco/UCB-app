package com.example.usecases

import com.example.data.NetworkResult
import com.example.data.UpdateRepository
import com.example.domain.Materia

class GetMateria(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(id: Int): NetworkResult<Materia> = updateRepository.getMateria(id)
}
