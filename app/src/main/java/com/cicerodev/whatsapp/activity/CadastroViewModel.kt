package com.cicerodev.whatsapp.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.whatsapp.model.Result
import com.cicerodev.whatsapp.model.Usuario
import com.cicerodev.whatsapp.repository.FirebaseRepositoryImp

class CadastroViewModel: ViewModel() {

    private val firebaseRepositoryImp = FirebaseRepositoryImp()
    private val cadastroLiveData: MutableLiveData<Result<Any?>> = MutableLiveData()

    fun cadastraUsuario(usuario: Usuario) {
        val resultLiveData = firebaseRepositoryImp.cadastrarUsuario(usuario)
        resultLiveData.observeForever { result ->
            cadastroLiveData.value = result
            resultLiveData.removeObserver { }
        }
    }

    fun getCadastroLiveData(): LiveData<Result<Any?>> {
        return cadastroLiveData
    }
}