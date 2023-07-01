package com.cicerodev.whatsapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cicerodev.whatsapp.R
import com.cicerodev.whatsapp.databinding.AdapterMensagemDestinatarioBinding
import com.cicerodev.whatsapp.databinding.AdapterMensagemRemetenteBinding
import com.cicerodev.whatsapp.model.Mensagem
import com.cicerodev.whatsapp.repository.FirebaseRepositoryImp

/**
 * Created by jamiltondamasceno
 */
class MensagensAdapter(
    private val mensagens: List<Mensagem>,
    private val context: Context
) : RecyclerView.Adapter<MensagensAdapter.MyViewHolder>() {
    private val firebaseRepositoryImp = FirebaseRepositoryImp()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == TIPO_REMETENTE) {
            val binding: AdapterMensagemRemetenteBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_mensagem_remetente, parent, false)
            MyViewHolder(binding)
        } else {
            val binding: AdapterMensagemDestinatarioBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.adapter_mensagem_destinatario, parent, false)
            MyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mensagem = mensagens[position]
        val msg = mensagem.mensagem
        val imagem = mensagem.imagem

        if (imagem != null) {
            val url = Uri.parse(imagem)
            Glide.with(context).load(url).into(holder.binding.root.findViewById(R.id.imageMensagemFoto))
            val nome = mensagem.nome
            if (!nome.isEmpty()) {
                holder.binding.root.findViewById<TextView>(R.id.textNomeExibicao)
            } else {
                holder.binding.root.findViewById<TextView>(R.id.textNomeExibicao).visibility = View.GONE
            }

            //Esconder o texto
            holder.binding.root.findViewById<TextView>(R.id.textMensagemTexto).visibility = View.GONE
        } else {
            holder.binding.root.findViewById<TextView>(R.id.textMensagemTexto).text = msg
            val nome = mensagem.nome
            if (!nome.isEmpty()) {
                holder.binding.root.findViewById<TextView>(R.id.textNomeExibicao).text = nome
            } else {
                holder.binding.root.findViewById<TextView>(R.id.textNomeExibicao).visibility = View.GONE
            }

            //Esconder a imagem
            holder.binding.root.findViewById<ImageView>(R.id.imageMensagemFoto).visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mensagens.size
    }

    override fun getItemViewType(position: Int): Int {
        val mensagem = mensagens[position]
        val idUsuario = firebaseRepositoryImp.getIdentificadorUsuario()
        return if (idUsuario == mensagem.idUsuario) {
            TIPO_REMETENTE
        } else {
            TIPO_DESTINATARIO
        }
    }

    inner class MyViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TIPO_REMETENTE = 0
        private const val TIPO_DESTINATARIO = 1
    }
}