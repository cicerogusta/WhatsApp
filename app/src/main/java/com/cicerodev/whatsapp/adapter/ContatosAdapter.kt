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
import com.cicerodev.whatsapp.model.Usuario
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by jamiltondamasceno
 */
class ContatosAdapter(val contatos: List<Usuario>, private val context: Context) :
    RecyclerView.Adapter<ContatosAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_contatos, parent, false)
        return MyViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val usuario = contatos[position]
        val cabecalho = usuario.email!!.isEmpty()
        holder.nome.text = usuario.nome
        holder.email.text = usuario.email
        if (usuario.foto != null) {
            val uri = Uri.parse(usuario.foto)
            Glide.with(context).load(uri).into(holder.foto)
        } else {
            if (cabecalho) {
                holder.foto.setImageResource(R.drawable.icone_grupo)
                holder.email.visibility = View.GONE
            } else {
                holder.foto.setImageResource(R.drawable.padrao)
            }
        }
    }

    override fun getItemCount(): Int {
        return contatos.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foto: CircleImageView
        var nome: TextView
        var email: TextView

        init {
            foto = itemView.findViewById(R.id.imageViewFotoContato)
            nome = itemView.findViewById(R.id.textNomeContato)
            email = itemView.findViewById(R.id.textEmailContato)
        }
    }
}