package com.cicerodev.whatsappcomdi.ui.fragments.grupo

import androidx.lifecycle.ViewModel
import com.cicerodev.whatsappcomdi.adapter.ContatosAdapter
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GrupoViewModel @Inject constructor(
    private val repository: FirebaseRepository
) : ViewModel() {

    fun recuperarContatosGrupo(listaMembros: MutableList<User>, adapter: ContatosAdapter) {
        repository.getContactsGroup(listaMembros, adapter)
    }

}