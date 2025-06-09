package com.example.usecases

import com.example.data.NetworkResult
import com.example.data.UpdateRepository
import com.example.domain.Elemento

class UpdateElemento(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(elemento: Elemento): NetworkResult<Boolean> = updateRepository.updateElemento(elemento)
}
