package com.example.data.subject

import com.example.domain.Subject

interface ISubjectRemoteDataSource {
    suspend fun fetch(userID: String): Subject
}