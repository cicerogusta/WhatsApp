package com.cicerodev.whatsappcomdi.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.cicerodev.whatsappcomdi.adapter.ContatosAdapter
import com.cicerodev.whatsappcomdi.data.model.Conversa
import com.cicerodev.whatsappcomdi.data.model.Grupo
import com.cicerodev.whatsappcomdi.data.model.Mensagem
import com.cicerodev.whatsappcomdi.data.model.User
import com.ciceropinheiro.whatsapp_clone.util.UiState
import com.ciceropinheiro.whatsapp_clone.util.codificarBase64
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class FirebaseRepositoryImp(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) : FirebaseRepository {
    override fun recuperarContatosGrupo(
        listaMembros: MutableList<User>,
        adapter: ContatosAdapter
    ) {
        val usuariosRef = database.reference.child("usuarios")
        usuariosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dados in dataSnapshot.children) {
                    val usuario = dados.getValue(User::class.java)
                    val emailUsuarioAtual = auth.currentUser?.email
                    if (usuario != null) {
                        if (emailUsuarioAtual != usuario.email) {
                            listaMembros.add(usuario)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun returnCurrentUser(): User {
        val firebaseUser = auth.currentUser
        val usuario = User()
        usuario.email = firebaseUser!!.email.toString()
        usuario.nome = firebaseUser.displayName.toString()
        if (firebaseUser.photoUrl == null) {
            usuario.foto = ""
        } else {
            usuario.foto = firebaseUser.photoUrl.toString()
        }
        return usuario
    }

    override fun loginUser(
        email: String,
        senha: String,
        result: (UiState<String>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Logado com Sucesso"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Falha ao logar, verifique email e senha"))
            }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Email has been sent"))

                } else {
                    result.invoke(UiState.Failure(task.exception?.message))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Authentication failed, Check email"))
            }
    }

    override fun logout() {
        auth.signOut()
    }


    override fun registerUser(user: User, result: (UiState<String>) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.senha).addOnCompleteListener {
            if (it.isSuccessful) {
                atualizarNomeUsuario(user.nome)
                database.reference.child("usuarios").child(getUserId()!!).setValue(user)
                result.invoke(UiState.Success("Registrado com Sucesso"))
            }

        }.addOnFailureListener {
            result.invoke(UiState.Failure(it.toString()))
        }


    }

    fun atualizarNomeUsuario(nome: String?): Boolean {
        return try {
            val user = auth.currentUser
            val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build()
            user!!.updateProfile(profile).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("Perfil", "Erro ao atualizar nome de perfil.")
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getUserProfileInDatabase(liveData: MutableLiveData<User>) {
        database.reference.child("usuarios").child(getUserId()!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    liveData.postValue(snapshot.getValue(User::class.java))
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    override fun getAllUsers(
        liveData: MutableLiveData<MutableList<User>>
    ) {
        database.reference.child("usuarios").addValueEventListener(object : ValueEventListener {
            val listUsuarios = mutableListOf<User>()

            override fun onDataChange(snapshot: DataSnapshot) {

                for (users in snapshot.children) {
                    val user = users.getValue(User::class.java)
                    if (user?.email != auth.currentUser?.email) {
                        if (user != null) {
                            listUsuarios.add(user)
                        }
                    }


                }
                val itemGrupo = User()
                itemGrupo.nome = "Novo grupo"
                itemGrupo.email = ""
                listUsuarios.add(itemGrupo)
                listUsuarios.reverse()
                liveData.value = listUsuarios

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun saveGroup(grupo: Grupo) {
        grupo.id = database.reference.child("grupos").push().key.toString()
        database.reference.child("grupos").child(grupo.id).setValue(grupo)
        for (membro in grupo.membros) {
            val idRemetente = codificarBase64(membro.email)
            val idDestinatario = grupo.id
            val conversa = Conversa()
            conversa.idRemetente = idRemetente
            conversa.idDestinatario = idDestinatario
            conversa.ultimaMensagem = ""
            conversa.isGroup = ("true")
            conversa.grupo = grupo
            saveConversa(conversa.idRemetente, conversa.idDestinatario, conversa)

        }
    }

    override fun isCurrentUser(): Boolean {
        var isCurrentUser = false
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            isCurrentUser = true

        }
        return isCurrentUser
    }

    override fun saveUserImageGalery(imagem: Uri, context: Context) {
        val storageReference =
            storage.reference.child("imagens")
                .child("perfil")
                .child(getUserId()!! + ".jpeg")


        val uploadTask = storageReference.putFile(imagem)
        uploadTask.addOnFailureListener {
            Toast.makeText(context, "ERRO: $it", Toast.LENGTH_SHORT).show()

        }.addOnSuccessListener {
            storageReference.downloadUrl.addOnCompleteListener {
                updateProfile(it.result)
                Toast.makeText(context, "IMAGEM INSERIDA COM SUCESSO", Toast.LENGTH_SHORT).show()


            }
        }

    }

    override fun saveGroupImageGalery(imagem: Uri, context: Context) {
        val storageReference =
            storage.reference.child("imagens")
                .child("grupo")
                .child(getUserId()!! + ".jpeg")


        val uploadTask = storageReference.putFile(imagem)
        uploadTask.addOnFailureListener {
            Toast.makeText(context, "ERRO: $it", Toast.LENGTH_SHORT).show()

        }.addOnSuccessListener {
            storageReference.downloadUrl.addOnCompleteListener {
                updateProfile(it.result)
                Toast.makeText(context, "IMAGEM INSERIDA COM SUCESSO", Toast.LENGTH_SHORT).show()


            }
        }

    }

    override fun saveUserImageCamera(imagem: Bitmap, context: Context) {

        //Recuperar dados da imagem para o firebase
        val baos = ByteArrayOutputStream()
        imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val dadosImagem = baos.toByteArray()
        val storageReference =
            storage.reference.child("imagens")
                .child("perfil")
                .child(getUserId()!! + ".jpeg")

        val uploadTask = storageReference.putBytes(dadosImagem)
        uploadTask.addOnFailureListener {
            Toast.makeText(context, "ERRO: $it", Toast.LENGTH_SHORT).show()

        }.addOnSuccessListener {
            storageReference.downloadUrl.addOnCompleteListener {
                updateProfile(it.result)
                Toast.makeText(context, "SUCESSO: AO CARREGAR IMAGEM", Toast.LENGTH_SHORT).show()


            }
        }

    }

    override fun getUserId(): String? {
        return auth.currentUser?.email?.let { codificarBase64(it) }
    }

    override fun sentMessage(idRemetente: String, idDestinatario: String, msg: Mensagem) {
        database.reference.child("mensagens").child(idRemetente).child(idDestinatario).push()
            .setValue(msg)
    }

    override fun getMessage(
        idRemetente: String,
        idDestinatario: String,
        livedata: MutableLiveData<MutableList<Mensagem>>
    ) {
        database.reference.child("mensagens").child(idRemetente).child(idDestinatario)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val mensagem = snapshot.getValue(Mensagem::class.java)
                    val listaMensagem = mutableListOf<Mensagem>()
                    if (mensagem != null) {
                        listaMensagem.add(mensagem)
                        livedata.value = listaMensagem
                    }


                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    override fun saveConversa(idRemetente: String?, idDestinatario: String?, conversaRemetente: Conversa) {
        val conversaRef = database.reference.child("conversas")
        if (idRemetente!=null) {
            if (idDestinatario!=null) {
                conversaRef.child(idRemetente)
                    .child(idDestinatario)
                    .setValue(conversaRemetente)
            }
        }

    }

    override fun getConversas(mutableLiveData: MutableLiveData<MutableList<Conversa>>) {
        val listaConversas = mutableListOf<Conversa>()

        database.reference.child("conversas").child(getUserId()!!).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val conversa = snapshot.getValue(Conversa::class.java)
                if (conversa != null) {
                    listaConversas.add(conversa)
                    mutableLiveData.postValue(listaConversas)
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
    }

    override fun getUserProfilePhoto(context: Context): Uri? {

        storage.reference.child("imagens").child("perfil")
            .child(getUserId()!! + ".jpeg").downloadUrl.addOnSuccessListener {
                updateProfile(it)
                Toast.makeText(context, "SUCESSO AO RECUPERAR IMAGEM", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "${auth.currentUser?.displayName}", Toast.LENGTH_SHORT)
                    .show()


            }.addOnFailureListener {
                Toast.makeText(context, "ERRO :$it", Toast.LENGTH_SHORT).show()
            }
        return auth.currentUser?.photoUrl

    }

    override fun updateProfile(url: Uri): Boolean {

        try {
            val user = auth.currentUser
            val profile = UserProfileChangeRequest.Builder().setPhotoUri(url).build()
            user?.updateProfile(profile)?.addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.d("Perfil", "Erro ao atualizar foto de perfil")
                    Log.d("Nome", "Nome: ${user.displayName}")

                }

            }

            return true

        } catch (e: Exception) {
            e.printStackTrace()
            return false

        }
    }

    override fun getNameUser(): String? {
        return auth.currentUser?.displayName

    }

    override fun updateUser(user: User) {
        database.reference.child("usuarios").child(getUserId()!!).updateChildren(user.map())
    }


}