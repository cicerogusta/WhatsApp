package com.cicerodev.whatsappcomdi.ui.fragments.grupo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.adapter.GrupoSelecionadoAdapter
import com.cicerodev.whatsappcomdi.data.model.Grupo
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.databinding.FragmentCadastroGrupoBinding
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.cicerodev.whatsappcomdi.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastroGrupoFragment : BaseFragment<FragmentCadastroGrupoBinding, CadastroGrupoViewModel>() {
    override val viewModel: CadastroGrupoViewModel by viewModels()
    private val listaMembrosSelecionados: MutableList<User> = ArrayList()
    private var grupoSelecionadoAdapter: GrupoSelecionadoAdapter? = null
    private val args: CadastroGrupoFragmentArgs by navArgs()
    private val gallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.salvarImagemGaleria(requireContext(), uri)
                binding.content.imageGrupo.setImageURI(uri)
            } else {
                binding.content.imageGrupo.setImageResource(R.drawable.padrao)
            }
        }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCadastroGrupoBinding = FragmentCadastroGrupoBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbar
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.title = "Novo grupo"
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }
        binding.content.imageGrupo.setOnClickListener {
            gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        recuperaListaMembro()
        binding.fabSalvarGrupo.setOnClickListener {
            val nomeGrupo: String = binding.content.editNomeGrupo.getText().toString()
            listaMembrosSelecionados.add(viewModel.retornaUsuarioAtual())
            val grupo = Grupo()
            grupo.membros = listaMembrosSelecionados
            grupo.nome = nomeGrupo
            viewModel.salvarGrupo(grupo)


        }
    }

    private fun recuperaListaMembro() {
        val listaMembros = args.listaMembosSelecionados.mutableList
        listaMembrosSelecionados.addAll(listaMembros)
        binding.content.textTotalParticipantes.text =
            "Participantes: " + listaMembrosSelecionados.size

        grupoSelecionadoAdapter =
            GrupoSelecionadoAdapter(listaMembrosSelecionados, requireContext())
        val layoutManagerHorizontal: RecyclerView.LayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.content.recyclerMembrosGrupo.setLayoutManager(layoutManagerHorizontal)
        binding.content.recyclerMembrosGrupo.setHasFixedSize(true)
        binding.content.recyclerMembrosGrupo.setAdapter(grupoSelecionadoAdapter)
    }
}