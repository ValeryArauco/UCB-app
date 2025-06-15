package com.medicat.usecases

import com.medicat.data.NetworkResult
import com.medicat.data.RecuperatorioRepository
import com.medicat.domain.Recuperatorio

class GetRecuperatorios(
    val recuperatorioRepository: RecuperatorioRepository,
) {
    suspend fun invoke(elementoId: String): NetworkResult<List<Recuperatorio>> =
        recuperatorioRepository.fetchRecuperatoriosByElemento(elementoId)
}
