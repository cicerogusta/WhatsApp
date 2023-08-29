package com.cicerodev.whatsappcomdi.ui.fragments.conversas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.adapter.ConversasAdapter
import com.cicerodev.whatsappcomdi.adapter.RecyclerItemClickListener
import com.cicerodev.whatsappcomdi.data.model.Conversa
import com.cicerodev.whatsappcomdi.databinding.FragmentConversasBinding
import com.cicerodev.whatsappcomdi.extensions.navigateTo
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.cicerodev.whatsappcomdi.ui.fragments.home.HomeFragmentDirections
import com.cicerodev.whatsappcomdi.util.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConversasFragment : BaseFragment<FragmentConversasBinding, ConversasViewModel>() {

    private lateinit var adapter: ConversasAdapter
    override val viewModel: ConversasViewModel by viewModels()
    var listaConveras = mutableListOf<Conversa>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentConversasBinding = FragmentConversasBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuraRecyclerViewListaConversas()
    }

    private fun configuraRecyclerViewListaConversas() {
        viewModel.conversa.observe(viewLifecycleOwner) { listaConversasLiveData ->
            //Configurar adapter
             adapter = ConversasAdapter(listaConversasLiveData, requireActivity())

            //Configurar recyclerview
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
            binding.recyclerListaConversas.setLayoutManager(layoutManager)
            binding.recyclerListaConversas.setHasFixedSize(true)
            binding.recyclerListaConversas.setAdapter(adapter)

            //Configurar evento de clique
            binding.recyclerListaConversas.addOnItemTouchListener(
                RecyclerItemClickListener(
                    activity,
                    binding.recyclerListaConversas,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View?, position: Int) {
                            val listaConversasAtualizada = adapter.conversas
                            val conversaSelecionada = listaConversasAtualizada[position]
                            if (!conversaSelecionada.equals(null)) {
                                if (conversaSelecionada.isGroup == "true") {
                                    navigateTo(HomeFragmentDirections.actionHomeFragmentToChatFragment(null, "chatGrupo", conversaSelecionada.grupo))

                                } else {
                                    navigateTo(HomeFragmentDirections.actionHomeFragmentToChatFragment(conversaSelecionada.usuarioExibicao, "chatContato", null))
                                }
                            }
                        }

                        override fun onItemClick(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                        }

                        override fun onLongItemClick(view: View?, position: Int) {

                        }

                    }
                )
            )
        }
    }

    fun pesquisarConversas(texto: String) {
        val listaConversasBusca = mutableListOf<Conversa>()
        for (conversa in viewModel.conversa.value!!) {
            val nome = conversa.usuarioExibicao?.nome?.lowercase()
            val ultimaMsg = conversa.ultimaMensagem.lowercase()

            if (nome?.contains(texto) == true || ultimaMsg.contains(texto)) {
               listaConversasBusca.add(conversa)

            }
        }
        adapter = ConversasAdapter(listaConversasBusca, requireContext())
        binding.recyclerListaConversas.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun recuperarConversas() {
        listaConveras = viewModel.conversa.value!!
        adapter = ConversasAdapter(listaConveras, requireContext())
        binding.recyclerListaConversas.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getConversas()
    }
}