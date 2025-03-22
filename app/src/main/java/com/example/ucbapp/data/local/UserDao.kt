package com.example.ucbapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ucbapp.data.model.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?
}