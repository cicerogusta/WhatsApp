package com.cicerodev.whatsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.cicerodev.whatsapp.databinding.ActivityCadastroBinding
import com.cicerodev.whatsapp.model.Usuario

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private val cadastroViewModel: CadastroViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button3.setOnClickListener {
            realizarCadastro()
        }
        cadastroViewModel.getCadastroLiveData().observe(this) {
            result ->
            if (result.success) {
                abrirTelaPrincipal()
            }
        }

    }

    private fun realizarCadastro() {
        val nome = binding.editNome.text.toString()
        val email = binding.editEmail.text.toString()
        val senha = binding.editSenha.text.toString()

        val usuario = Usuario()
        usuario.email = email
        usuario.senha = senha
        usuario.nome = nome
        cadastroViewModel.cadastraUsuario(usuario)

    }

    private fun abrirTelaPrincipal() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}