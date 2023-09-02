package com.cicerodev.whatsappcomdi.ui.fragments.chat

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.whatsappcomdi.data.model.Conversa
import com.cicerodev.whatsappcomdi.data.model.Grupo
import com.cicerodev.whatsappcomdi.data.model.Mensagem
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepository
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatFragmentViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    private val _mensagens = MutableLiveData<MutableList<Mensagem>>()
    val mensagens: MutableLiveData<MutableList<Mensagem>>
        get() = _mensagens

    fun retornaIdRemetente(): String? {
        return repository.getUserId()
    }

    fun getDownloadUrl(imagem: Bitmap, userId: String): MutableLiveData<String> {
        return repository.getDownloadUrl(imagem, userId)
    }

    fun enviaMensagem(idRemetente: String, idDestinatario: String, msg: Mensagem) {
        repository.sentMessage(idRemetente, idDestinatario, msg)
    }

    fun retornaMensagem(idRemetente: String, idDestinatario: String) {
        repository.getMessage(idRemetente, idDestinatario, _mensagens)
    }

    fun retornaUsuarioLogado() = repository.returnCurrentUser()

    fun salvaConversa(
        conversa: Conversa
    ) {
        repository.saveConversa(
            conversa
        )
    }
}