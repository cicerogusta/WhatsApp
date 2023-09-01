package com.cicerodev.whatsappcomdi.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.adapter.TabViewPagerAdapter
import com.cicerodev.whatsappcomdi.databinding.FragmentHomeBinding
import com.cicerodev.whatsappcomdi.extensions.navigateTo
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.cicerodev.whatsappcomdi.ui.fragments.contatos.ContatosFragment
import com.cicerodev.whatsappcomdi.ui.fragments.conversas.ConversasFragment
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: TabViewPagerAdapter
    override val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.mainToolbar.toolbarPrincipal.title = "WhatsApp"
        binding.viewPagerTab.setTabTextColors(
            resources.getColor(R.color.branco),
            resources.getColor(R.color.branco)
        )
        setupMenuToolbar()
    }

    private fun setupMenuToolbar() {
        binding.mainToolbar.toolbarPrincipal.inflateMenu(R.menu.menu_main)
        setupSearchView()

    }

    private fun setupSearchView() {
        val searchItem: MenuItem =
            binding.mainToolbar.toolbarPrincipal.menu.findItem(R.id.menuPesquisa)
        val searchView: SearchView? = searchItem.actionView as SearchView?
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                when (viewPager.currentItem) {

                    0 -> {
                        val fragment = adapter.fragments[0] as ConversasFragment
                        fragment.recuperarConversas()
                    }
                }
                return true
            }
        })

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                when (viewPager.currentItem) {
                    0 -> {
                        val fragment = adapter.fragments[0] as ConversasFragment
                        if (newText.isNotEmpty()) {
                            fragment.pesquisarConversas(newText.lowercase())
                        } else {
                            fragment.recuperarConversas()
                        }
                    }

                    1 -> {
                        val fragment = adapter.fragments[1] as ContatosFragment
                        if (newText.isNotEmpty()) {
                            fragment.pesquisarContatos(newText.lowercase())
                        } else {
                            fragment.recuperarContatos()
                        }
                    }
                }
                return true
            }
        })
    }

    private fun setupViewPager() {
        val tabLayout = binding.viewPagerTab
        viewPager = binding.viewPager
        adapter = TabViewPagerAdapter(this)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(adapter.tabs[position])
        }.attach()
    }

    override fun setupClickListener() {
        binding.mainToolbar.toolbarPrincipal.setOnMenuItemClickListener { item ->
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


}