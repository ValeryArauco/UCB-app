package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.UpdateRepository
import com.medicat.domain.Recuperatorio

class UpdateRecuperatorio(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(recuperatorio: Recuperatorio): NetworkResult<Boolean> = updateRepository.updateRecuperatorio(recuperatorio)
}
