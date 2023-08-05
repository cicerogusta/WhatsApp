package com.cicerodev.whatsappcomdi.ui.activity

import androidx.lifecycle.ViewModel
import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    fun deslogaUsuario() {
        repository.logout()

    }


    }
