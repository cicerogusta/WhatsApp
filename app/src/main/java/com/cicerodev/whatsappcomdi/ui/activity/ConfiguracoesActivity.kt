package com.cicerodev.whatsappcomdi.ui.activity

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.databinding.ActivityConfiguracoesBinding
import com.cicerodev.whatsappcomdi.ui.base.BaseActivity
import com.cicerodev.whatsappcomdi.util.Permissao
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfiguracoesActivity :
    BaseActivity<ConfiguracoesActivityViewModel, ActivityConfiguracoesBinding>() {
    private var uri: Uri? = null
    private val permissoesNecessarias = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    private var user: User = User()
    override val viewModel: ConfiguracoesActivityViewModel by viewModels()
    private val gallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.salvarImagemGaleria(this, uri)
                binding.circleImageViewFotoPerfil.setImageURI(uri)
            } else {
                binding.circleImageViewFotoPerfil.setImageResource(R.drawable.padrao)
            }
        }


    private val camera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                    val bitmap = result.data?.extras?.get("data") as Bitmap
                    viewModel.salvarImagemCamera(this, bitmap)
                    binding.circleImageViewFotoPerfil.setImageBitmap(bitmap)
                }
            }
        }

    override fun getViewBinding(): ActivityConfiguracoesBinding =
        ActivityConfiguracoesBinding.inflate(layoutInflater)

    override fun setupClickListener() {
        binding.imageButtonCamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            camera.launch(cameraIntent)
        }

        binding.imageButtonGaleria.setOnClickListener {
            gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

        binding.imageAtualizarNome.setOnClickListener {
            val userUpdated = User(
                user.id,
                binding.editPerfilNome.text.toString(),
                user.email,
                user.senha,
                uri.toString()
            )

            viewModel.atualizarUsuario(userUpdated)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
        Permissao.validarPermissoes(permissoesNecessarias, this, 1)
        setupToolbar()
        observer()
        viewModel.pegaPerfilUsuario()

        uri = viewModel.pegaPerfilFotoUsuario(this)
        if (uri != null) {
            Glide.with(this).load(uri.toString()).into(binding.circleImageViewFotoPerfil)


        } else {
            binding.circleImageViewFotoPerfil.setImageResource(R.drawable.padrao)

        }
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbarConfig.toolbarPrincipal
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (premissaoResultado in grantResults) {
            if (premissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao()
            }
        }
    }

    private fun alertaValidacaoPermissao() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissões Negadas")
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Confirmar",
            DialogInterface.OnClickListener { dialogInterface, i ->
                finish()
            })

        val dialog = builder.create()
        dialog.show()
    }

    private fun observer() {
        viewModel.register.observe(this) {
            if (it != null) {

                user = it
                binding.editPerfilNome.setText(user.nome)

            } else {
                Toast.makeText(this, "VAZIO", Toast.LENGTH_LONG).show()

            }
        }
    }

}