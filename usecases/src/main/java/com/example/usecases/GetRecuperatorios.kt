package com.example.usecases

import com.example.data.NetworkResult
import com.example.data.RecuperatorioRepository
import com.example.domain.Recuperatorio

class GetRecuperatorios(
    val recuperatorioRepository: RecuperatorioRepository,
) {
    suspend fun invoke(elementoId: String): NetworkResult<List<Recuperatorio>> =
        recuperatorioRepository.fetchRecuperatoriosByElemento(elementoId)
}
