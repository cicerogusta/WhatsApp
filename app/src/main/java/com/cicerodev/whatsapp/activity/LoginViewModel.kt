package com.cicerodev.whatsapp.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.whatsapp.model.Result
import com.cicerodev.whatsapp.model.Usuario
import com.cicerodev.whatsapp.repository.FirebaseRepositoryImp
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

    private val firebaseRepositoryImp: FirebaseRepositoryImp = FirebaseRepositoryImp()
    private val loginLiveData: MutableLiveData<Result<Any?>> = MutableLiveData()

    fun getLoginLiveData(): LiveData<Result<Any?>> {
        return loginLiveData
    }

    fun loginUsuario(usuario: Usuario) {
        val resultLiveData = firebaseRepositoryImp.loginUsuario(usuario)
        resultLiveData.observeForever { result ->
            loginLiveData.value = result
            resultLiveData.removeObserver { }
        }
    }
    fun retornaUsuarioAtual(): FirebaseUser? {
        return firebaseRepositoryImp.getUsuarioAtual()
    }
}