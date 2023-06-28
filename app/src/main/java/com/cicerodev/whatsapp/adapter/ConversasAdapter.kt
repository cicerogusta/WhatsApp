package com.cicerodev.whatsapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cicerodev.whatsapp.R
import com.cicerodev.whatsapp.model.Conversa
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by jamiltondamasceno
 */
class ConversasAdapter(val conversas: List<Conversa>, private val context: Context) :
    RecyclerView.Adapter<ConversasAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_contatos, parent, false)
        return MyViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val conversa = conversas[position]
        holder.ultimaMensagem.text = conversa.ultimaMensagem
        if (conversa.isGroup == "true") {
            val grupo = conversa.grupo
            holder.nome.text = grupo!!.nome
            if (grupo.foto != null) {
                val uri = Uri.parse(grupo.foto)
                Glide.with(context).load(uri).into(holder.foto)
            } else {
                holder.foto.setImageResource(R.drawable.padrao)
            }
        } else {
            val usuario = conversa.usuarioExibicao
            if (usuario != null) {
                holder.nome.text = usuario.nome
                if (usuario.foto != null) {
                    val uri = Uri.parse(usuario.foto)
                    Glide.with(context).load(uri).into(holder.foto)
                } else {
                    holder.foto.setImageResource(R.drawable.padrao)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return conversas.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foto: CircleImageView
        var nome: TextView
        var ultimaMensagem: TextView

        init {
            foto = itemView.findViewById(R.id.imageViewFotoContato)
            nome = itemView.findViewById(R.id.textNomeContato)
            ultimaMensagem = itemView.findViewById(R.id.textEmailContato)
        }
    }
}