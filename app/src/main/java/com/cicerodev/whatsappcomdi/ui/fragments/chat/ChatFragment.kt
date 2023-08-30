package com.cicerodev.whatsappcomdi.ui.fragments.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.adapter.MensagensAdapter
import com.cicerodev.whatsappcomdi.data.model.Mensagem
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.databinding.FragmentChatBinding
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.ciceropinheiro.whatsapp_clone.util.codificarBase64


class ChatFragment : BaseFragment<FragmentChatBinding, ChatFragmentViewModel>() {
    private var idRemetenteGrupo: String = ""
    private lateinit var bitmap: Bitmap
    private var idUsuarioRemetente: String? = null
    private var usuarioDestinatario: User? = null
    override val viewModel: ChatFragmentViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val args: ChatFragmentArgs by navArgs()
    private var listaMensagens = mutableListOf<Mensagem>()
    private lateinit var idUsuarioDestinatario: String

    private val camera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                     bitmap = result.data?.extras?.get("data") as Bitmap
                    val mensagem = Mensagem()
                    mensagem.idUsuario = idUsuarioRemetente
                    mensagem.mensagem = "imagem.jpeg"
                    viewModel.getDownloadUrl(bitmap, idUsuarioRemetente!!).observe(viewLifecycleOwner) { downloadUrl ->
                        mensagem.imagem = downloadUrl
                        idUsuarioRemetente?.let { viewModel.enviaMensagem(it, idUsuarioDestinatario, mensagem) }
                        idUsuarioRemetente?.let { viewModel.enviaMensagem(idUsuarioDestinatario, it, mensagem) }

                    }


                }
            }
        }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding = FragmentChatBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.toolbar
        toolbar.title = ""
        val appCompatActivity = (activity as AppCompatActivity?)!!
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        idUsuarioRemetente = viewModel.retornaIdRemetente()

        viewModel.mensagens.value?.clear()
        observer()
        recuperaDadosUsuarioDestinatario()

        binding.content.fabEnviar.setOnClickListener {
            enviarMensagem()
            binding.content.editMensagem.setText("")

        }

        binding.content.imageCamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            camera.launch(cameraIntent)


        }

    }

    private fun enviarMensagem() {
        val textoMensagem = binding.content.editMensagem.text.toString()
        if (textoMensagem.isNotEmpty()) {
            if (usuarioDestinatario != null) {
                val mensagem = Mensagem()
                mensagem.idUsuario = idUsuarioRemetente
                mensagem.mensagem = textoMensagem

                viewModel.enviaMensagem(idUsuarioRemetente!!, idUsuarioDestinatario, mensagem)
                viewModel.enviaMensagem(idUsuarioDestinatario, idUsuarioRemetente!!, mensagem)
                viewModel.salvaConversa(
                    idUsuarioRemetente!!,
                    idUsuarioDestinatario,
                    usuarioDestinatario!!,
                    mensagem,
                    false,
                    null
                )
                viewModel.salvaConversa(
                    idUsuarioDestinatario,
                    idUsuarioRemetente!!,
                    viewModel.retornaUsuarioLogado(),
                    mensagem,
                    false,
                    null
                )


            } else {

                for (membro in args.grupo?.membros!!) {
                     idRemetenteGrupo = codificarBase64(membro.email)
                    val idUsuarioLogadoGrupo: String = viewModel.retornaIdRemetente().toString()
                    val mensagem = Mensagem()
                    mensagem.idUsuario = (idUsuarioLogadoGrupo)
                    mensagem.mensagem = (textoMensagem)
                    mensagem.nome = (viewModel.retornaUsuarioLogado().nome)

                    //salvar mensagem para o membro
                    viewModel.enviaMensagem(idRemetenteGrupo, idUsuarioDestinatario, mensagem)

                    //Salvar conversa
                        viewModel.salvaConversa(
                            idRemetenteGrupo,
                            idUsuarioDestinatario,
                            usuarioDestinatario,
                            mensagem,
                            true, args.grupo
                        )
                }
            }

        }
    }

    private fun recuperaDadosUsuarioDestinatario() {
        if (args.tipoChat.equals("chatGrupo")) {
            val grupo = args.grupo
            if (grupo != null) {
                idUsuarioDestinatario = grupo.id
            }
            if (grupo != null) {
                binding.textViewNomeChat.setText(grupo.nome)
            }
            val foto = grupo?.foto
            if (!foto.equals("")) {
                val url = Uri.parse(foto)
                Glide.with(this).load(url).into(binding.circleImageFotoChat)
            } else {
                binding.circleImageFotoChat.setImageResource(R.drawable.padrao)
            }
        } else {
            usuarioDestinatario = args.user
            binding.textViewNomeChat.setText(usuarioDestinatario?.nome)
            val foto = usuarioDestinatario?.foto
            if (!foto.equals("")) {
                val url = Uri.parse(usuarioDestinatario?.foto ?: "")
                Glide.with(this)
                    .load(url)
                    .into(binding.circleImageFotoChat)

            } else {
                binding.circleImageFotoChat.setImageResource(R.drawable.padrao)
            }


            //recuperar dados usuario destinatario
            idUsuarioDestinatario = usuarioDestinatario?.email?.let { codificarBase64(it) }.toString()
        }
    }

    private fun observer() {
        viewModel.mensagens.observe(viewLifecycleOwner) {
            it.forEach { mensagem ->
                listaMensagens.add(mensagem)
            }
            configuraRecyclerView()
        }
    }


    private fun configuraRecyclerView() {
        binding.content.recyclerMensagens.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter =
                MensagensAdapter(listaMensagens, requireContext(), viewModel.retornaIdRemetente()!!)
            this.adapter?.notifyDataSetChanged()

        }
    }

    override fun onStart() {
        super.onStart()
        configuraRecyclerView()
        viewModel.retornaMensagem(idUsuarioRemetente.toString(), idUsuarioDestinatario)
//        viewModel.retornaDownloadUrl(bitmap).toString()

    }

    override fun onStop() {
        super.onStop()
        viewModel.mensagens.value?.clear()
    }
}