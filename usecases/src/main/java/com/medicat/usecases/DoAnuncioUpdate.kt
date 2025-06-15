package com.medicat.usecases

import com.medicat.data.RealDatabaseRepository
import kotlinx.coroutines.flow.Flow

class DoAnuncioUpdate(
    val repository: RealDatabaseRepository,
) {
    fun invoke(): Flow<String> = repository.getAnuncioUpdates()
}
