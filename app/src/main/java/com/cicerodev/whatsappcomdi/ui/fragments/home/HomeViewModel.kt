package com.ciceropinheiro.whatsapp_clone.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FirebaseRepository

) : ViewModel() {

    fun deslogaUsuario() {
        repository.logout()

    }
}