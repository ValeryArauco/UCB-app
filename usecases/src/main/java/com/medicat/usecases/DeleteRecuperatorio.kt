package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.UpdateRepository

class DeleteRecuperatorio(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(recuperatorioId: Int): NetworkResult<Boolean> = updateRepository.deleteRecuperatorio(recuperatorioId)
}
