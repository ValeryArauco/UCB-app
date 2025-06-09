package com.example.usecases

import com.example.data.NetworkResult
import com.example.data.UpdateRepository
import com.example.domain.Recuperatorio

class UpdateRecuperatorio(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(recuperatorio: Recuperatorio): NetworkResult<Boolean> = updateRepository.updateRecuperatorio(recuperatorio)
}
