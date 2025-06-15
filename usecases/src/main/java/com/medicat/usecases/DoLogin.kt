package com.medicat.usecases

import com.medicat.data.MateriaRepository
import kotlinx.coroutines.delay

class DoLogin(
    val materiaRepository: MateriaRepository,
) {
    suspend fun invoke(
        userName: String,
        password: String,
    ): Result<Unit> {
        delay(1)
        return return Result.success(Unit)
    }
}
