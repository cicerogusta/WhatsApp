package com.cicerodev.whatsappcomdi.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.ui.fragments.contatos.ContatosFragment
import com.cicerodev.whatsappcomdi.ui.fragments.conversas.ConversasFragment

class TabViewPagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    val tabs = arrayOf(R.string.conversas, R.string.contatos)
    val fragments = arrayOf(ConversasFragment(), ContatosFragment())
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}