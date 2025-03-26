package com.example.ucbapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.persistence.DAOs.UserDao
import com.example.data.persistence.entities.UserEntity
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