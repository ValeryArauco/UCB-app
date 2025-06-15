package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.UpdateRepository

class UpdateSaber(
    val updateRepository: UpdateRepository,
) {
    suspend fun invoke(saberId: Int): NetworkResult<Boolean> = updateRepository.updateSaber(saberId)
}
