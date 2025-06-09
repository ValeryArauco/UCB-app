package com.example.usecases

import com.example.data.NetworkResult
import com.example.data.SaberRepository
import com.example.domain.Saber

class GetSaberes(
    val saberRepository: SaberRepository,
) {
    suspend fun invoke(elementoId: String): NetworkResult<List<Saber>> = saberRepository.fetchSaberByElemento(elementoId)
}
