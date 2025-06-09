package com.example.usecases

import com.example.data.ElementoRepository
import com.example.data.NetworkResult
import com.example.domain.Elemento


class GetElementos(
    val elementoRepository: ElementoRepository
) {
    suspend fun invoke(materiaId: String): NetworkResult<List<Elemento>> = elementoRepository.fetchElementosByMateria(materiaId)
}