package com.example.usecases

import com.example.data.NetworkResult
import com.example.data.UpdateRepository

class DeleteRecuperatorio(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(recuperatorioId: Int): NetworkResult<Boolean> = updateRepository.deleteRecuperatorio(recuperatorioId)
}
