package com.medicat.usecases

import com.medicat.data.ElementoRepository
import com.medicat.data.NetworkResult
import com.medicat.domain.Elemento


class GetElementos(
    val elementoRepository: ElementoRepository
) {
    suspend fun invoke(materiaId: String): NetworkResult<List<Elemento>> = elementoRepository.fetchElementosByMateria(materiaId)
}