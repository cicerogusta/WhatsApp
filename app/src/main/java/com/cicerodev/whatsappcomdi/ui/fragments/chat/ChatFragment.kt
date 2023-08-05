package com.cicerodev.whatsappcomdi.ui.fragments.chat

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.adapter.MensagensAdapter
import com.cicerodev.whatsappcomdi.data.model.Mensagem
import com.cicerodev.whatsappcomdi.databinding.FragmentChatBinding
import com.cicerodev.whatsappcomdi.ui.activity.MainActivity
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.ciceropinheiro.whatsapp_clone.util.codificarBase64


class ChatFragment : BaseFragment<FragmentChatBinding, ChatFragmentViewModel>() {
    override val viewModel: ChatFragmentViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val args: ChatFragmentArgs by navArgs()
    private var listaMensagens = mutableListOf<Mensagem>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding = FragmentChatBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.toolbar
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.title = ""
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }

        viewModel.retornaMensagem(
            viewModel.retornaIdRemetente()!!,
            codificarBase64(args.user.email)
        )

        observer()
        recuperaDadosUsuarioDestinatario()

        binding.content.fabEnviar.setOnClickListener {
            if (binding.content.editMensagem.text.toString().isNotEmpty()) {
                val mensagem = Mensagem()
                mensagem.idUsuario = viewModel.retornaIdRemetente()
                mensagem.mensagem = binding.content.editMensagem.text.toString()
                viewModel.enviaMensagem(
                    viewModel.retornaIdRemetente()!!,
                    codificarBase64(args.user.email),
                    mensagem
                )
                viewModel.enviaMensagem(
                    codificarBase64(args.user.email),
                    viewModel.retornaIdRemetente()!!,
                    mensagem
                )
                binding.content.editMensagem.setText("")

            }

        }

    }

    private fun recuperaDadosUsuarioDestinatario() {
        binding.user = args.user
        if (args.user.foto!= null) {
            val url = Uri.parse(args.user.foto)
            Glide.with(this)
                .load(url)
                .into(binding.circleImageFotoChat)
        } else {
            binding.circleImageFotoChat.setImageResource(R.drawable.padrao)
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