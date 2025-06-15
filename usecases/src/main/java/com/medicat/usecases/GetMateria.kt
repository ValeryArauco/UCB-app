package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.UpdateRepository
import com.medicat.domain.Materia

class GetMateria(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(id: Int): NetworkResult<Materia> = updateRepository.getMateria(id)
}
