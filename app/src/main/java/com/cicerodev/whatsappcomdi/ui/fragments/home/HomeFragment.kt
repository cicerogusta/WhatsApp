package com.cicerodev.whatsappcomdi.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.adapter.TabViewPagerAdapter
import com.cicerodev.whatsappcomdi.databinding.FragmentHomeBinding
import com.cicerodev.whatsappcomdi.extensions.navigateTo
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainToolbar.toolbarPrincipal.title ="WhatsApp"
        binding.viewPagerTab.setTabTextColors(resources.getColor(R.color.branco), resources.getColor(R.color.branco))
        setupMenuToolbar(binding.mainToolbar.toolbarPrincipal)
        setupViews()
    }

    private fun setupMenuToolbar(toolbar: Toolbar) {
        toolbar.inflateMenu(R.menu.menu_main)
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
        val adapter = TabViewPagerAdapter(this)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(adapter.tabs[position])
        }.attach()
    }



}