package com.cicerodev.whatsappcomdi.ui.activity

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfiguracoesActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    private val _user = MutableLiveData<User>()
    val register: MutableLiveData<User>
        get() = _user


    fun salvarImagemGaleria(context: Context, uri: Uri) {
        repository.saveUserImageGalery(uri, context)

    }

    fun pegaPerfilFotoUsuario(context: Context): Uri? {
        return repository.getUserProfilePhoto(context)
    }

    fun salvarImagemCamera(context: Context, bitmap: Bitmap) {
        repository.saveUserImageCamera(bitmap, context)

    }

    fun pegaPerfilUsuario() {
        repository.getUserProfileInDatabase(_user)
    }

    fun atualizarUsuario(user: User) {
        repository.updateUser(user)
    }

}
