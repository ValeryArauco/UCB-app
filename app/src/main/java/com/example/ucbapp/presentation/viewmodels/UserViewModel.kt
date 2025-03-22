package com.example.ucbapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucbapp.data.local.UserDao
import com.example.ucbapp.data.model.UserEntity
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