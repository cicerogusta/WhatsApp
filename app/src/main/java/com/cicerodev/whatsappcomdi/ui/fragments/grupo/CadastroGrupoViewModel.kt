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
class CadastroGrupoViewModel@Inject constructor(
    private val repository: FirebaseRepository
) : ViewModel() {

    fun salvarImagemGaleria(context: Context, uri: Uri) {
        repository.saveUserImageGalery(uri, context)

    }

    fun retornaUsuarioAtual() : User = repository.returnCurrentUser()

    fun salvarGrupo(grupo: Grupo) {
        repository.saveGroup(grupo)
    }
}