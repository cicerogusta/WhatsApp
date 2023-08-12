package com.cicerodev.whatsappcomdi.ui.fragments.conversas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.whatsappcomdi.data.model.Conversa
import com.cicerodev.whatsappcomdi.data.model.Grupo
import com.cicerodev.whatsappcomdi.data.model.Mensagem
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConversasViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    private val _conversas = MutableLiveData<MutableList<Conversa>>()
    val conversa: MutableLiveData<MutableList<Conversa>>
        get() = _conversas

    fun getConversas() {
        repository.getConversas(_conversas)
    }
}