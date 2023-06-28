package com.cicerodev.whatsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cicerodev.whatsapp.model.Mensagem
import com.cicerodev.whatsapp.model.Result
import com.cicerodev.whatsapp.model.Usuario
import com.google.firebase.auth.FirebaseUser

interface IFirebaseRepository {
    fun loginUsuario(usuario: Usuario): LiveData<Result<Any?>>
    fun cadastrarUsuario(usuario: Usuario): MutableLiveData<Result<Any?>>
    fun atualizarNomeUsuario(nome: String?): Boolean
    fun salvarUsuarioFirebase(usuario: Usuario)
    fun getIdentificadorUsuario(): String
    fun recuperarContatos(): MutableLiveData<MutableList<Usuario>>
    fun getUsuarioAtual(): FirebaseUser?
    fun salvarMensagem(idRemetente: String, idDestinatario: String, msg: Mensagem)
    fun recuperarMensagens(idRemetente: String, idDestinatario: String): MutableLiveData<MutableList<Mensagem>>
}