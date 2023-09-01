package com.cicerodev.whatsappcomdi.ui.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.databinding.FragmentCadastroBinding
import com.cicerodev.whatsappcomdi.ui.base.BaseFragment
import com.cicerodev.whatsappcomdi.util.isValidEmail
import com.cicerodev.whatsappcomdi.util.toast
import com.cicerodev.whatsappcomdi.util.UiState
import com.cicerodev.whatsappcomdi.util.codificarBase64

class RegisterFragment : BaseFragment<FragmentCadastroBinding, RegisterViewModel>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCadastroBinding = FragmentCadastroBinding.inflate(layoutInflater, container, false)

    override fun setupClickListener() {
        binding.buttonCadastrar.setOnClickListener {
            signUpUser()
        }
    }

    lateinit var navigation: NavDirections
    override val viewModel: RegisterViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener()

    }

    private fun signUpUser() {
        if (validation()) {
            try {
                val usuario = User(
                    nome = binding.editNome.text.toString(),
                    email = binding.editEmail.text.toString(),
                    senha = binding.editSenha.text.toString(),
                    foto = ""
                )
                val idUsuario = codificarBase64(usuario.email)
                usuario.id = idUsuario
                viewModel.registerUser(usuario)

                observer()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.editNome.text.isNullOrEmpty()) {
            isValid = false
            toast("Email não pode estar vazio!")

        } else {
            if (binding.editEmail.text.isNullOrEmpty()) {
                isValid = false
                toast("Email não pode estar vazio!")
            } else {
                if (!binding.editEmail.text.toString().isValidEmail()) {
                    isValid = false
                    toast("Insira um email valido")
                } else {
                    if (binding.editSenha.text.isNullOrEmpty()) {
                        isValid = false
                        toast("Senha não pode estar vazia")
                    } else {
                        if (binding.editSenha.text.toString().length < 8) {
                            isValid = false
                            toast("A senha deve conter 6 caracteres")
                        }
                    }
                }
            }

        }

        return isValid
    }

    private fun callFragment(navigation: NavDirections) {
        findNavController().navigate(navigation)
    }

    private fun observer() {
        viewModel.register.observe(viewLifecycleOwner) { state ->
            when (state) {
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
                    navigation = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                    callFragment(navigation)
                }
            }
        }
    }

}