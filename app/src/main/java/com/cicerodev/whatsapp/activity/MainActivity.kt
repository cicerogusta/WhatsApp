package com.cicerodev.whatsapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cicerodev.whatsapp.R
import com.cicerodev.whatsapp.databinding.ActivityMainBinding
import com.cicerodev.whatsapp.fragment.ContatosFragment
import com.cicerodev.whatsapp.fragment.ConversasFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainToolbar.toolbarPrincipal.title = getString(R.string.app_name)
        setSupportActionBar(binding.mainToolbar.toolbarPrincipal)


        //Configurar abas
        val fragments: MutableList<Fragment> = ArrayList()
        fragments.add(ConversasFragment())
        fragments.add(ContatosFragment())
        val adapter: FragmentStateAdapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }

            override fun getItemCount(): Int {
                return fragments.size
            }
        }
        binding.viewPager.setAdapter(adapter)

        TabLayoutMediator(
            binding.viewPagerTab, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.text = "Conversas"
                1 -> tab.text = "Contatos"
            }
        }.attach()
    }
}