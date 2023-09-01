package com.cicerodev.whatsappcomdi.ui.fragments.grupo

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.cicerodev.whatsappcomdi.data.model.Grupo
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CadastroGrupoViewModel @Inject constructor(
    private val repository: FirebaseRepository
) : ViewModel() {

    fun salvarImagemGrupoGaleria(context: Context, uri: Uri, grupo: Grupo) {
        repository.saveGroupImageGalery(uri, context, grupo)
    }

    fun retornaUsuarioAtual(): User = repository.returnCurrentUser()

    fun salvarGrupo(grupo: Grupo) {
        repository.saveGroup(grupo)
    }
}