package com.cicerodev.whatsappcomdi.ui.fragments.conversas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.cicerodev.whatsappcomdi.databinding.FragmentConversasBinding
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment


class ConversasFragment : BaseFragment<FragmentConversasBinding, ConversasViewModel>() {

    override val viewModel: ConversasViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentConversasBinding = FragmentConversasBinding.inflate(inflater, container, false)
}