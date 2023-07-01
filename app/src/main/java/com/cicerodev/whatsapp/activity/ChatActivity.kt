package com.cicerodev.whatsapp.activity

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cicerodev.whatsapp.R
import com.cicerodev.whatsapp.adapter.MensagensAdapter
import com.cicerodev.whatsapp.databinding.ActivityChatBinding
import com.cicerodev.whatsapp.helper.Base64Custom
import com.cicerodev.whatsapp.model.Grupo
import com.cicerodev.whatsapp.model.Mensagem
import com.cicerodev.whatsapp.model.Usuario
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var adapter: MensagensAdapter
    private lateinit var binding: ActivityChatBinding
    private var grupo: Grupo? = null
    private var usuarioDestinatario: Usuario? = null
    private val usuarioRemetente: Usuario? = null
    private var idUsuarioRemetente: String = ""
    private var idUsuarioDestinatario: String = ""
    private val chatViewModel: ChatViewModel by viewModels()
    private var listaMensagens: MutableList<Mensagem> = ArrayList<Mensagem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val toolbar = binding.toolbar
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        idUsuarioRemetente = chatViewModel.retornaIdentificadorUsuario()
        recuperarDadosDestinatario()
        binding.content.fabEnviar.setOnClickListener {
            enviarMensagem()
        }




    }

    private fun enviarMensagem() {
        val textoMensagem: String = binding.content.editMensagem.text.toString()
        val mensagem = Mensagem()
        mensagem.idUsuario = idUsuarioRemetente
        mensagem.mensagem = textoMensagem

        chatViewModel.salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem)
        chatViewModel.salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem)
    }

    private fun recuperarDadosDestinatario() {

        //Recuperar dados do usuário destinatario
        val bundle = intent.extras
        if (bundle != null) {

            if (bundle.containsKey("chatGrupo")) {
                grupo = bundle.getSerializable("chatGrupo") as Grupo?
                idUsuarioDestinatario = grupo?.id.toString()
                binding.textViewNomeChat.setText(grupo?.nome ?: "")
                val foto: String? = grupo?.foto
                if (foto != null) {
                    val url = Uri.parse(foto)
                    Glide.with(this@ChatActivity)
                        .load(url)
                        .into(binding.circleImageFotoChat)
                } else {
                    binding.circleImageFotoChat.setImageResource(R.drawable.padrao)
                }
            } else {
                /** */
                usuarioDestinatario = bundle.getSerializable("chatContato") as Usuario?
                binding.textViewNomeChat.setText(usuarioDestinatario?.nome)
                val foto: String? = usuarioDestinatario?.foto
                if (foto != null) {
                    val url = Uri.parse(usuarioDestinatario?.foto)
                    Glide.with(this@ChatActivity)
                        .load(url)
                        .into(binding.circleImageFotoChat)
                } else {
                    binding.circleImageFotoChat.setImageResource(R.drawable.padrao)
                }

                //recuperar dados usuario destinatario
                idUsuarioDestinatario =
                    Base64Custom.codificarBase64(usuarioDestinatario?.email ?: "")
                /** */
            }
        }
    }

    override fun onStart() {
        super.onStart()
        chatViewModel.recuperarMensagens(idUsuarioRemetente, idUsuarioDestinatario)
        chatViewModel.recuperarMensagens(
            idUsuarioRemetente,
            idUsuarioDestinatario
        ).observe(this) { mensagemMutableList ->
            if (mensagemMutableList.isNotEmpty()) {
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                listaMensagens = mensagemMutableList
            } else {
                Toast.makeText(this, "VAZIO", Toast.LENGTH_SHORT).show()
            }
            adapter = MensagensAdapter(listaMensagens, this)
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
            binding.content.recyclerMensagens.setLayoutManager(layoutManager)
            binding.content.recyclerMensagens.setHasFixedSize(true)
            binding.content.recyclerMensagens.setAdapter(adapter)

        }

    }
}