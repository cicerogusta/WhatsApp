package com.cicerodev.whatsapp.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.whatsapp.model.Mensagem
import com.cicerodev.whatsapp.repository.FirebaseRepositoryImp

class ChatViewModel: ViewModel() {
    private val firebaseRepositoryImp: FirebaseRepositoryImp = FirebaseRepositoryImp()

    fun retornaIdentificadorUsuario(): String {
        return firebaseRepositoryImp.getIdentificadorUsuario()
    }

     fun salvarMensagem(idRemetente: String, idDestinatario: String, msg: Mensagem) {
         firebaseRepositoryImp.salvarMensagem(idRemetente, idDestinatario, msg)
     }
    fun recuperarMensagens(idRemetente: String, idDestinatario: String): MutableLiveData<MutableList<Mensagem>> {
        return firebaseRepositoryImp.recuperarMensagens(idRemetente, idDestinatario)
    }
}