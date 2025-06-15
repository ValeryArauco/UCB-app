package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.SaberRepository
import com.medicat.domain.Saber

class GetSaberes(
    val saberRepository: SaberRepository,
) {
    suspend fun invoke(elementoId: String): NetworkResult<List<Saber>> = saberRepository.fetchSaberByElemento(elementoId)
}
