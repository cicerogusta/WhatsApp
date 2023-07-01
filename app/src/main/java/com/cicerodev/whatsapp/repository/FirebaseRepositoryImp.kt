package com.cicerodev.whatsapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cicerodev.whatsapp.helper.Base64Custom
import com.cicerodev.whatsapp.model.Mensagem
import com.cicerodev.whatsapp.model.Result
import com.cicerodev.whatsapp.model.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseRepositoryImp() : IFirebaseRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference()
    override fun loginUsuario(usuario: Usuario): LiveData<Result<Any?>> {
        val loginLiveData: MutableLiveData<Result<Any?>> = MutableLiveData()

        usuario.email?.let {
            usuario.senha?.let { it1 ->
                firebaseAuth.signInWithEmailAndPassword(it, it1)
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            loginLiveData.value = Result(success = true)
                        } else {
                            val exception = task.exception
                            val errorMessage = when (exception) {
                                is FirebaseAuthInvalidUserException -> "Usuário não está cadastrado."
                                is FirebaseAuthInvalidCredentialsException -> "E-mail e senha não correspondem a um usuário cadastrado."
                                else -> "Erro ao cadastrar usuário: ${exception?.message}"
                            }
                            loginLiveData.value = Result(
                                success = false,
                                exception = exception,
                                errorMessage = errorMessage
                            )
                        }
                    })
            }
        }

        return loginLiveData
    }

    override fun cadastrarUsuario(usuario: Usuario): MutableLiveData<Result<Any?>> {
        val cadastroLiveData: MutableLiveData<Result<Any?>> = MutableLiveData()

        usuario.email?.let {
            usuario.senha?.let { it1 ->
                firebaseAuth.createUserWithEmailAndPassword(it, it1)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            atualizarNomeUsuario(usuario.nome)
                            try {
                                val identificadorUsuario =
                                    Base64Custom.codificarBase64(usuario.email)
                                usuario.id = identificadorUsuario
                                salvarUsuarioFirebase(usuario)
                                cadastroLiveData.value = Result(true)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                cadastroLiveData.value = Result(false, e)
                            }
                        } else {
                            val excecao = when (task.exception) {
                                is FirebaseAuthWeakPasswordException -> "Digite uma senha mais forte!"
                                is FirebaseAuthInvalidCredentialsException -> "Por favor, digite um e-mail válido"
                                is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada"
                                else -> "Erro ao cadastrar usuário: ${task.exception?.message}"
                            }
                            cadastroLiveData.value =
                                Result(false, task.exception, errorMessage = excecao)
                        }
                    }
            }
        }

        return cadastroLiveData
    }

    override fun atualizarNomeUsuario(nome: String?): Boolean {
        return try {
            val user: FirebaseUser? = getUsuarioAtual()
            val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build()
            user?.updateProfile(profile)?.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("Perfil", "Erro ao atualizar nome de perfil.")
                }
            }
            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun salvarUsuarioFirebase(usuario: Usuario) {
        usuario.id?.let { database.child("usuarios").child(it).setValue(usuario) }
    }

    override fun getIdentificadorUsuario(): String {
        return Base64Custom.codificarBase64(getUsuarioAtual()?.email ?: "")
    }

    override fun recuperarContatos(): MutableLiveData<MutableList<Usuario>> {
        val listaContatos = mutableListOf<Usuario>()
        val listaContatosLiveData: MutableLiveData<MutableList<Usuario>> = MutableLiveData()
        val usuariosRef = database.child("usuarios")
        usuariosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dados in dataSnapshot.children) {
                    val usuario = dados.getValue(Usuario::class.java)
                    val emailUsuarioAtual: String? = getUsuarioAtual()?.email
                    if (emailUsuarioAtual != usuario!!.email) {
                        listaContatos.add(usuario)
                        listaContatosLiveData.value = listaContatos
                    }
                }
                val itemGrupo = Usuario()
                itemGrupo.nome = "Novo grupo"
                itemGrupo.email = ""
                listaContatos.add(itemGrupo)
//                listaContatos.reverse()

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return listaContatosLiveData
    }

    override fun getUsuarioAtual(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun salvarMensagem(idRemetente: String, idDestinatario: String, msg: Mensagem) {
        database.child("mensagens").child(idRemetente).child(idDestinatario).push().setValue(msg)
    }

    override fun recuperarMensagens(
        idRemetente: String,
        idDestinatario: String
    ): MutableLiveData<MutableList<Mensagem>> {
        val listaMensagens = mutableListOf<Mensagem>()
        val mtbListaMensagens: MutableLiveData<MutableList<Mensagem>> = MutableLiveData()
        val mensagensRef = database.child("mensagens").child(idRemetente).child(idDestinatario)
        mensagensRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val mensagem = snapshot.getValue(Mensagem::class.java)
                if (mensagem != null) {
                    listaMensagens.add(mensagem)
                    mtbListaMensagens.value = listaMensagens

                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return mtbListaMensagens
    }
}