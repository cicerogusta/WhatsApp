package com.cicerodev.whatsappcomdi.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.databinding.FragmentLoginBinding
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.cicerodev.whatsappcomdi.data.model.User
import com.ciceropinheiro.whatsapp_clone.util.UiState
import com.cicerodev.whatsappcomdi.util.toast

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    lateinit var navigation: NavDirections
    override val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding  = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners(binding)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCurrentUser()
        observerCurrentUser()
    }

    private fun observerCurrentUser() {
        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it == true) {
                navigation = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                callFragment(navigation)
            }
        }
    }

    private fun setupListeners(binding: FragmentLoginBinding) {
        binding.txtCadastro.isClickable = true
        binding.txtCadastro.setOnClickListener {
            navigation = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            callFragment(navigation)
        }

        binding.buttonLogar.setOnClickListener {
            if (validation()) {
                val user = User(email = binding.editLoginEmail.text.toString(), senha = binding.editLoginSenha.text.toString())
                viewModel.login(user.email, user.senha)
                observer()
            }
        }
    }

    private fun callFragment(navigation: NavDirections) {
        findNavController().navigate(navigation)
    }

    fun observer(){
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
//                    binding.loginProgress.show()
                }
                is UiState.Failure -> {
//                    binding.loginProgress.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
//                    binding.loginProgress.hide()
                    toast(state.data)
                    navigation = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    callFragment(navigation)
                }
            }
        }
    }

    fun validation(): Boolean {
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