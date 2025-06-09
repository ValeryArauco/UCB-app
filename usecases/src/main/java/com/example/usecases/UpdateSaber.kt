package com.example.usecases

import com.example.data.NetworkResult
import com.example.data.UpdateRepository

class UpdateSaber(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(saberId: Int): NetworkResult<Boolean> = updateRepository.updateSaber(saberId)
}
