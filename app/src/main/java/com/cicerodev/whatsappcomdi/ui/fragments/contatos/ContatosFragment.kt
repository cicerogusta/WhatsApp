package com.cicerodev.whatsappcomdi.ui.fragments.contatos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.adapter.ContatosAdapter
import com.cicerodev.whatsappcomdi.adapter.RecyclerItemClickListener
import com.cicerodev.whatsappcomdi.databinding.FragmentContatosBinding
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.extensions.navigateTo
import com.cicerodev.whatsappcomdi.ui.fragments.home.HomeFragmentDirections

class ContatosFragment : BaseFragment<FragmentContatosBinding, ContatosViewModel>() {
    private lateinit var listaContatos: MutableList<User>
    private lateinit var contatosAdapter: ContatosAdapter
    lateinit var mActivity: FragmentActivity
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContatosBinding = FragmentContatosBinding.inflate(inflater, container, false)

    override val viewModel: ContatosViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllUsers()
        observer()

    }

    private fun observer() {
        viewModel.users.observe(viewLifecycleOwner) {
            configurarRecyclerView(it)
        }
    }

    private fun configurarRecyclerView(it: MutableList<User>) {
        binding.recyclerViewListaContatos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            contatosAdapter = ContatosAdapter(it, requireContext())
            adapter = contatosAdapter
        }
        binding.recyclerViewListaContatos.addOnItemTouchListener(
            RecyclerItemClickListener(
                activity,
                binding.recyclerViewListaContatos,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val cabecalho = it[position].email.isEmpty()
                        if (cabecalho) {
                            navigateTo(HomeFragmentDirections.actionHomeFragmentToGrupoFragment())
                        } else {
                            val tipoChat = "chatContato"
                            navigateTo(
                                HomeFragmentDirections.actionHomeFragmentToChatFragment(
                                    it[position],
                                    tipoChat,
                                    null
                                )
                            )

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

                })
        )
    }

    fun pesquisarContatos(texto: String) {
        val listaContatosBusca = mutableListOf<User>()
        for (contato in viewModel.users.value!!) {

            val nome = contato.nome.lowercase()

            if (nome.contains(texto)) {
                listaContatosBusca.add(contato)

            }

        }
        contatosAdapter = ContatosAdapter(listaContatosBusca, requireContext())
        binding.recyclerViewListaContatos.adapter = contatosAdapter
        contatosAdapter.notifyDataSetChanged()
    }

    fun recuperarContatos() {
        listaContatos = viewModel.users.value!!
        contatosAdapter = ContatosAdapter(listaContatos, requireContext())
        binding.recyclerViewListaContatos.adapter = contatosAdapter
        contatosAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let { mActivity = it }
    }
}



