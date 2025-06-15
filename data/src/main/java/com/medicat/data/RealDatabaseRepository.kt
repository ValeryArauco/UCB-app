package com.medicat.data

import com.medicat.data.anuncio.IRealDatabaseDataSource
import kotlinx.coroutines.flow.Flow

class RealDatabaseRepository(
    val dataSource: IRealDatabaseDataSource,
) {
    fun getAnuncioUpdates(): Flow<String> = dataSource.getAnunciosUpdates()
}
