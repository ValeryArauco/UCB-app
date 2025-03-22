package com.ucb.framework.persistence.DAOs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: com.ucb.framework.persistence.entities.UserEntity)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): com.ucb.framework.persistence.entities.UserEntity?
}