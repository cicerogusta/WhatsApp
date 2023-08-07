package com.cicerodev.whatsappcomdi.ui.fragments.grupo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.databinding.FragmentContatosBinding
import com.cicerodev.whatsappcomdi.databinding.FragmentGrupoBinding
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.cicerodev.whatsappcomdi.ui.fragments.contatos.ContatosViewModel

class GrupoFragment : BaseFragment<FragmentGrupoBinding, GrupoViewModel>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGrupoBinding = FragmentGrupoBinding.inflate(inflater, container, false)

    override val viewModel: GrupoViewModel by hiltNavGraphViewModels(R.id.nav_graph)

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
    }
}