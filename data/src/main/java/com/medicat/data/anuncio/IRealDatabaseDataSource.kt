package com.medicat.data.anuncio

import kotlinx.coroutines.flow.Flow

interface IRealDatabaseDataSource {
    fun getAnunciosUpdates(): Flow<String>
}
