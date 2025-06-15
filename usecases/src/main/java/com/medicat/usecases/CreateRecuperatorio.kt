package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.UpdateRepository
import com.medicat.domain.Recuperatorio

class CreateRecuperatorio(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(recuperatorio: Recuperatorio): NetworkResult<Boolean> = updateRepository.createRecuperatorio(recuperatorio)
}
