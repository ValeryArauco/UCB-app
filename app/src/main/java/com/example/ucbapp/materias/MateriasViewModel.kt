package com.example.ucbapp.materias

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.usecases.GetMaterias
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


//@HiltViewModel
//class MateriasViewModel @Inject constructor (
//    private val getMaterias: GetMaterias,
//    @ApplicationContext private val context: Context
//): ViewModel(){}