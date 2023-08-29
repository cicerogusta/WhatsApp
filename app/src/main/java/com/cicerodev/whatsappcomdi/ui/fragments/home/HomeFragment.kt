package com.cicerodev.whatsappcomdi.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.adapter.TabViewPagerAdapter
import com.cicerodev.whatsappcomdi.databinding.FragmentHomeBinding
import com.cicerodev.whatsappcomdi.extensions.navigateTo
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.cicerodev.whatsappcomdi.ui.fragments.conversas.ConversasFragment
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    private lateinit var adapter: TabViewPagerAdapter
    override val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainToolbar.toolbarPrincipal.title ="WhatsApp"
        binding.viewPagerTab.setTabTextColors(resources.getColor(R.color.branco), resources.getColor(R.color.branco))
        setupMenuToolbar(binding.mainToolbar.toolbarPrincipal)
        setupViews()
    }

    private fun setupMenuToolbar(toolbar: Toolbar) {
        toolbar.inflateMenu(R.menu.menu_main)
        val searchItem: MenuItem = toolbar.menu.findItem(R.id.menuPesquisa)
        val searchView: SearchView? = searchItem.actionView as SearchView?
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // O SearchView foi expandido (ícone da lupa foi clicado)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // O SearchView foi retraído (ícone da lupa foi clicado novamente)
                // Aqui você pode lidar com o comportamento do botão "Voltar"
                val fragment = adapter.fragments[0] as ConversasFragment
                fragment.recuperarConversas()
                return true
            }
        })

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Aqui você pode lidar com a submissão da consulta de pesquisa
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
               val fragment = adapter.fragments[0] as ConversasFragment
                if (newText.isNotEmpty()) {
                    fragment.pesquisarConversas(newText.lowercase())
                } else {
                    fragment.recuperarConversas()
                }
                return true
            }
        })
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuSair -> {
                    viewModel.deslogaUsuario()
                    navigateTo(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                }
                R.id.menuConfiguracoes -> {
                    navigateTo(HomeFragmentDirections.actionHomeFragmentToConfiguracoesActivity())
                }
            }
            true

        }
    }

    private fun setupViews() {
        val tabLayout = binding.viewPagerTab
        val viewPager = binding.viewPager
         adapter = TabViewPagerAdapter(this)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(adapter.tabs[position])
        }.attach()
    }



}