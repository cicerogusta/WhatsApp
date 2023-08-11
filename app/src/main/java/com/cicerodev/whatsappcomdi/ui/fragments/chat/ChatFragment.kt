package com.cicerodev.whatsappcomdi.ui.fragments.chat

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var usuarioDestinatario: User
    override val viewModel: ChatFragmentViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val args: ChatFragmentArgs by navArgs()
    private var listaMensagens = mutableListOf<Mensagem>()
    private lateinit var idUsuarioDestinatario: String

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


        args.user?.let { codificarBase64(it.email) }?.let {
            viewModel.retornaMensagem(
                viewModel.retornaIdRemetente()!!,
                it
            )
        }

        observer()
        recuperaDadosUsuarioDestinatario()

        binding.content.fabEnviar.setOnClickListener {
            if (binding.content.editMensagem.text.toString().isNotEmpty()) {
                val mensagem = Mensagem()
                mensagem.idUsuario = viewModel.retornaIdRemetente()
                mensagem.mensagem = binding.content.editMensagem.text.toString()
                args.user?.let { it1 -> codificarBase64(it1.email) }?.let { it2 ->
                    viewModel.enviaMensagem(
                        viewModel.retornaIdRemetente()!!,
                        it2,
                        mensagem
                    )
                }
                args.user?.let { it1 -> codificarBase64(it1.email) }?.let { it2 ->
                    viewModel.enviaMensagem(
                        it2,
                        viewModel.retornaIdRemetente()!!,
                        mensagem
                    )
                }
                binding.content.editMensagem.setText("")

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
            usuarioDestinatario = args.user!!
            binding.textViewNomeChat.setText(usuarioDestinatario.nome)
            val foto = usuarioDestinatario.foto
            if (!foto.equals("")) {
                val url = Uri.parse(usuarioDestinatario.foto)
                Glide.with(this)
                    .load(url)
                    .into(binding.circleImageFotoChat)

            } else {
                binding.circleImageFotoChat.setImageResource(R.drawable.padrao)
            }


            //recuperar dados usuario destinatario
            idUsuarioDestinatario = codificarBase64(usuarioDestinatario.email)
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


    }
}