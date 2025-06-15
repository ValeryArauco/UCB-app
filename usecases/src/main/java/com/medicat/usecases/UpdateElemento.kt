package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.UpdateRepository
import com.medicat.domain.Elemento

class UpdateElemento(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(elemento: Elemento): NetworkResult<Boolean> = updateRepository.updateElemento(elemento)
}
