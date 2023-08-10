package com.cicerodev.whatsappcomdi.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.data.model.User
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by jamiltondamasceno
 */
class GrupoSelecionadoAdapter(
    private val contatosSelecionados: MutableList<User>,
    private val context: Context
) : RecyclerView.Adapter<GrupoSelecionadoAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_grupo_selecionado, parent, false)
        return MyViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val usuario = contatosSelecionados[position]
        holder.nome.text = usuario.nome
        if (usuario.foto != null) {
            val uri = Uri.parse(usuario.foto)
            Glide.with(context).load(uri).into(holder.foto)
        } else {
            holder.foto.setImageResource(R.drawable.padrao)
        }
    }

    override fun getItemCount(): Int {
        return contatosSelecionados.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foto: CircleImageView
        var nome: TextView

        init {
            foto = itemView.findViewById(R.id.imageViewFotoMembroSelecionado)
            nome = itemView.findViewById(R.id.textNomeMembroSelecionado)
        }
    }
}