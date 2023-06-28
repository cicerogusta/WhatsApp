package com.cicerodev.whatsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cicerodev.whatsapp.R
import com.cicerodev.whatsapp.databinding.ActivityLoginBinding
import com.cicerodev.whatsapp.model.Usuario

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.button2.setOnClickListener {
            realizarLogin()
        }
        binding.textView.setOnClickListener {
            abrirTelaCadastro()
        }
        loginViewModel.getLoginLiveData().observe(this, Observer {
                result ->
            if (result.success) {
                abrirTelaPrincipal()
            } else {
                Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun realizarLogin() {
        val email = binding.editLoginEmail.text.toString()
        val senha = binding.editLoginSenha.text.toString()

        if (email.isNotEmpty() && senha.isNotEmpty()) {
            val usuario = Usuario()
            usuario.email = email
            usuario.senha = senha
            loginViewModel.loginUsuario(usuario)
        } else {
            Toast.makeText(this, "Preencha o email e a senha", Toast.LENGTH_SHORT).show()
        }
    }

    private fun abrirTelaPrincipal() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun abrirTelaCadastro() {
        val intent = Intent(this, CadastroActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (loginViewModel.retornaUsuarioAtual()?.equals(null) == false) {
            abrirTelaPrincipal()
        }
    }
}