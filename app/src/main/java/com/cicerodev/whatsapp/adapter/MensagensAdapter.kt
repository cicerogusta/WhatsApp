package com.cicerodev.whatsapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cicerodev.whatsapp.R
import com.cicerodev.whatsapp.model.Mensagem
import com.cicerodev.whatsapp.repository.FirebaseRepositoryImp

/**
 * Created by jamiltondamasceno
 */
class MensagensAdapter(lista: List<Mensagem>, c: Context) :
    RecyclerView.Adapter<MensagensAdapter.MyViewHolder?>() {
    private val firebaseRepositoryImp: FirebaseRepositoryImp = FirebaseRepositoryImp()


    private val mensagens: List<Mensagem>
    private val context: Context

    init {
        mensagens = lista
        context = c
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var item: View? = null
        if (viewType == TIPO_REMETENTE) {
            item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mensagem_remetente, parent, false)
        } else if (viewType == TIPO_DESTINATARIO) {
            item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mensagem_destinatario, parent, false)
        }
        return MyViewHolder(item!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mensagem: Mensagem = mensagens[position]
        val msg: String? = mensagem.mensagem
        val imagem: String? = mensagem.imagem
        if (imagem != null) {
            val url = Uri.parse(imagem)
            Glide.with(context).load(url).into(holder.imagem)
            val nome: String? = mensagem.nome
            if (nome != null) {
                if (!nome.isEmpty()) {
                    holder.nome.setText(nome)
                } else {
                    holder.nome.setVisibility(View.GONE)
                }
            }

            //Esconder o texto
            holder.mensagem.setVisibility(View.GONE)
        } else {
            holder.mensagem.setText(msg)
            val nome: String? = mensagem.nome
            if (nome != null) {
                if (!nome.isEmpty()) {
                    holder.nome.setText(nome)
                } else {
                    holder.nome.setVisibility(View.GONE)
                }
            }

            //Esconder a imagem
            holder.imagem.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mensagens.size
    }

    override fun getItemViewType(position: Int): Int {
        val mensagem: Mensagem = mensagens[position]
        val idUsuario: String = firebaseRepositoryImp.getIdentificadorUsuario()
        return if (idUsuario == mensagem.idUsuario) {
            TIPO_REMETENTE
        } else TIPO_DESTINATARIO
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mensagem: TextView
        var nome: TextView
        var imagem: ImageView

        init {
            mensagem = itemView.findViewById<TextView>(R.id.textMensagemTexto)
            imagem = itemView.findViewById(R.id.imageMensagemFoto)
            nome = itemView.findViewById<TextView>(R.id.textNomeExibicao)
        }
    }

    companion object {
        private const val TIPO_REMETENTE = 0
        private const val TIPO_DESTINATARIO = 1
    }
}