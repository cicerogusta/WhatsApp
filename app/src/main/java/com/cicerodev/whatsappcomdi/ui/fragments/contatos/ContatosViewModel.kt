package com.cicerodev.whatsappcomdi.ui.fragments.contatos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContatosViewModel @Inject constructor(private val repository: FirebaseRepository): ViewModel() {

    private val _users = MutableLiveData<MutableList<User>>()
    val users: MutableLiveData<MutableList<User>>
        get() = _users

    fun getAllUsers() {
         repository.getAllUsers(_users)
    }

}
