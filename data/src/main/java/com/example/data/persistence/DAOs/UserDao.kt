package com.example.data.persistence.DAOs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.persistence.entities.UserEntity

@Dao
interface UserDao {
    // Insertar un usuario
    @Insert
    suspend fun insertUser(user: UserEntity)

    // Buscar usuario por ID
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    // Métodos adicionales que pueden ser útiles
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Int)
}