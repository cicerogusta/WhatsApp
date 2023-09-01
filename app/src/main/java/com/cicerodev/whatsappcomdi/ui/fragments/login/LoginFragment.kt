package com.cicerodev.whatsappcomdi.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.databinding.FragmentLoginBinding
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.cicerodev.whatsappcomdi.util.toast
import com.cicerodev.whatsappcomdi.util.UiState

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    private lateinit var navDirections: NavDirections
    override val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun setupClickListener() {
        binding.txtCadastro.isClickable = true
        binding.txtCadastro.setOnClickListener {
            navDirections = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            callFragment(navDirections)
        }

        binding.buttonLogar.setOnClickListener {
            if (validation()) {
                val user = User(
                    email = binding.editLoginEmail.text.toString(),
                    senha = binding.editLoginSenha.text.toString()
                )
                viewModel.login(user.email, user.senha)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupClickListener()
    }

    override fun onStart() {
        super.onStart()
        observerLoginUser()
        viewModel.getCurrentUser()
        observerCurrentUser()
    }

    private fun observerCurrentUser() {
        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it == true) {
                navDirections = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                callFragment(navDirections)
            }
        }
    }

    private fun callFragment(navigation: NavDirections) {
        findNavController().navigate(navigation)
    }

    private fun observerLoginUser() {
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }

                is UiState.Failure -> {
                    toast(state.error)
                }

                is UiState.Success -> {
                    toast(state.data)
                    navDirections = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    callFragment(navDirections)
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.editLoginEmail.text.isNullOrEmpty()) {
            isValid = false
            toast("Email não pode estar vazio!")

        } else {
            if (binding.editLoginSenha.text.isNullOrEmpty()) {
                isValid = false
                toast("Senha não pode estar vazio!")
            }

        }

        return isValid
    }

}