package com.cicerodev.whatsappcomdi.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.cicerodev.whatsappcomdi.adapter.ContatosAdapter
import com.cicerodev.whatsappcomdi.data.model.Conversa
import com.cicerodev.whatsappcomdi.data.model.Grupo
import com.cicerodev.whatsappcomdi.data.model.Mensagem
import com.cicerodev.whatsappcomdi.data.model.User
import com.ciceropinheiro.whatsapp_clone.util.UiState

interface FirebaseRepository {
    fun loginUser(email: String, senha: String, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
    fun logout()
    fun registerUser(user: User, result: (UiState<String>) -> Unit)
    fun getUserProfileInDatabase(liveData: MutableLiveData<User>)
    fun getAllUsers(
        liveData: MutableLiveData<MutableList<User>>
    )

    fun saveConversa(
        idRemetente: String,
        idDestinatario: String,
        usuarioExibicao: User,
        mensagem: Mensagem,
        isGroup: Boolean,
        grupo: Grupo?
    )

    fun saveGroup(grupo: Grupo)
    fun recuperarContatosGrupo(
        listaMembros: MutableList<User>,
        adapter: ContatosAdapter
    )

    fun returnCurrentUser(): User
    fun isCurrentUser(): Boolean
    fun saveUserImageGalery(imagem: Uri, context: Context)
    fun saveGroupImageGalery(imagem: Uri, context: Context)
    fun getUserId(): String?
    fun sentMessage(idRemetente: String, idDestinatario: String, msg: Mensagem)
    fun getMessage(
        idRemetente: String,
        idDestinatario: String,
        livedata: MutableLiveData<MutableList<Mensagem>>
    )

    fun saveConversaGrupo(
        idRemetente: String?,
        idDestinatario: String?,
        conversaRemetente: Conversa
    )

    fun getConversas(mutableLiveData: MutableLiveData<MutableList<Conversa>>)


    fun updateProfile(url: Uri): Boolean
    fun getUserProfilePhoto(context: Context): Uri?
    fun saveUserImageCamera(imagem: Bitmap, context: Context)
    fun getNameUser(): String?
    fun updateUser(user: User)
}