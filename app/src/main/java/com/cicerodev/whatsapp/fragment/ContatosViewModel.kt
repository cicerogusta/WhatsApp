package com.cicerodev.whatsapp.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.whatsapp.model.Usuario
import com.cicerodev.whatsapp.repository.FirebaseRepositoryImp
import com.google.firebase.auth.FirebaseUser

class ContatosViewModel : ViewModel() {
    private val firebaseRepositoryImp = FirebaseRepositoryImp()

    fun recuperarContatos(): MutableLiveData<MutableList<Usuario>> {
        return firebaseRepositoryImp.recuperarContatos()
    }

    fun getUsuarioAtual(): FirebaseUser? {
        return firebaseRepositoryImp.getUsuarioAtual()
    }
}