package com.example.usecases

import com.example.data.RealDatabaseRepository
import kotlinx.coroutines.flow.Flow

class DoAnuncioUpdate(
    val repository: RealDatabaseRepository,
) {
    fun invoke(): Flow<String> = repository.getAnuncioUpdates()
}
