package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.UpdateRepository

class UpdateMateria(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(
        id: Int,
        recTomados: Int,
        elemCompletados: Int,
        elemEvaluados: Int,
        recTotales: Int,
    ): NetworkResult<Boolean> = updateRepository.updateMateria(id, recTomados, elemCompletados, elemEvaluados, recTotales)
}
