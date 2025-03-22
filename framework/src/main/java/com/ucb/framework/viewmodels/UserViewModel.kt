package com.uob.framework.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.framework.persistence.DAOs.UserDao
import com.ucb.framework.persistence.entities.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    fun addUser(user: UserEntity) {
        viewModelScope.launch {
            userDao.insertUser(user)
        }
    }

    suspend fun getUserById(id: Int): UserEntity? {
        return userDao.getUserById(id)
    }
}