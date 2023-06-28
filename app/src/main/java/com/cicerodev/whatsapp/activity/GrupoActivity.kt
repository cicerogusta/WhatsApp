package com.cicerodev.whatsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cicerodev.whatsapp.databinding.ActivityChatBinding
import com.cicerodev.whatsapp.databinding.ActivityGrupoBinding

class GrupoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGrupoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGrupoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}