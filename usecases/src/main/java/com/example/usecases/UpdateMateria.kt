package com.example.usecases

import com.example.data.NetworkResult
import com.example.data.UpdateRepository

class UpdateMateria(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(
        id: Int,
        recTomados: Int,
        elemCompletados: Int,
        elemEvaluados: Int,
    ): NetworkResult<Boolean> = updateRepository.updateMateria(id, recTomados, elemCompletados, elemEvaluados)
}
