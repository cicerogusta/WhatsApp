package com.cicerodev.whatsappcomdi.ui.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepository
import com.ciceropinheiro.whatsapp_clone.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val  repository: FirebaseRepository

) : ViewModel() {
    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    private val _currentUser = MutableLiveData<Boolean>()
    val currentUser: LiveData<Boolean>
        get() = _currentUser

    fun login(
        email: String,
        senha : String
    ) {
        _login.value = UiState.Loading
        repository.loginUser(
            email, senha
        ) {
            _login.value = it
        }
    }

    fun getCurrentUser() {
        _currentUser.value = repository.isCurrentUser()
    }
}