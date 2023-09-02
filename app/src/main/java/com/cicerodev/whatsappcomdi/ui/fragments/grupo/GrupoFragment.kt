package com.cicerodev.whatsappcomdi.ui.fragments.grupo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cicerodev.whatsappcomdi.adapter.ContatosAdapter
import com.cicerodev.whatsappcomdi.adapter.GrupoSelecionadoAdapter
import com.cicerodev.whatsappcomdi.adapter.RecyclerItemClickListener
import com.cicerodev.whatsappcomdi.data.model.MutableListType
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.databinding.FragmentGrupoBinding
import com.cicerodev.whatsappcomdi.extensions.navigateTo
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GrupoFragment : BaseFragment<FragmentGrupoBinding, GrupoViewModel>() {

    private val listaMembrosSelecionados: MutableList<User> = ArrayList()
    private lateinit var grupoSelecionadoAdapter: GrupoSelecionadoAdapter
    private lateinit var contatosAdapter: ContatosAdapter
    private val listaMembros: MutableList<User> = ArrayList()
    override val viewModel: GrupoViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGrupoBinding = FragmentGrupoBinding.inflate(inflater, container, false)

    override fun setupClickListener() {

        binding.content.recyclerMembrosSelecionados.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                binding.content.recyclerMembrosSelecionados,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val usuarioSelecionado = listaMembrosSelecionados[position]

                        //Remover da listagem de membros selecionados
                        listaMembrosSelecionados.remove(usuarioSelecionado)
                        grupoSelecionadoAdapter!!.notifyDataSetChanged()

                        //Adicionar à listagem de membros
                        listaMembros.add(usuarioSelecionado)
                        contatosAdapter!!.notifyDataSetChanged()
                        atualizarMembrosToolbar()
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

        binding.fabAvancarCadastro.setOnClickListener {
            navigateTo(
                GrupoFragmentDirections.actionGrupoFragmentToCadastroGrupoFragment2(
                    MutableListType(listaMembrosSelecionados)
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener()
        setupToolbar()
        configuraRecyclerViewContatos()
        configuraRecyclerMembrosSelecioados()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        toolbar.title = "Novo grupo"
        val appCompatActivity = (activity as AppCompatActivity?)!!
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            navigateTo(GrupoFragmentDirections.actionGrupoFragmentToHomeFragment())
        }
    }

    private fun atualizarMembrosToolbar() {
        val totalSelecionados = listaMembrosSelecionados.size
        val total = listaMembros.size + totalSelecionados
        binding.toolbar.subtitle = "$totalSelecionados de $total selecionados"
    }

    private fun configuraRecyclerViewContatos() {
        contatosAdapter = ContatosAdapter(listaMembros, requireContext())


        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding.content.recyclerMembros.setLayoutManager(layoutManager)
        binding.content.recyclerMembros.setHasFixedSize(true)
        binding.content.recyclerMembros.setAdapter(contatosAdapter)
        binding.content.recyclerMembros.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                binding.content.recyclerMembros,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val usuarioSelecionado = listaMembros[position]


                        listaMembros.remove(usuarioSelecionado)
                        contatosAdapter!!.notifyDataSetChanged()


                        listaMembrosSelecionados.add(usuarioSelecionado)
                        grupoSelecionadoAdapter!!.notifyDataSetChanged()
                        atualizarMembrosToolbar()
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

    private fun configuraRecyclerMembrosSelecioados() {
        grupoSelecionadoAdapter =
            GrupoSelecionadoAdapter(listaMembrosSelecionados, requireContext())
        val layoutManagerHorizontal: RecyclerView.LayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.content.recyclerMembrosSelecionados.setLayoutManager(layoutManagerHorizontal)
        binding.content.recyclerMembrosSelecionados.setHasFixedSize(true)
        binding.content.recyclerMembrosSelecionados.setAdapter(grupoSelecionadoAdapter)

    }

    private fun recuperarContatosGrupo() {
        viewModel.recuperarContatosGrupo(listaMembros, contatosAdapter)
        atualizarMembrosToolbar()

    }

    override fun onStart() {
        super.onStart()
        recuperarContatosGrupo()
    }
}