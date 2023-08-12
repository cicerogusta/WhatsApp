package com.cicerodev.whatsappcomdi.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.data.model.User
import com.cicerodev.whatsappcomdi.databinding.AdapterContatosBinding

class ContatosAdapter(
    private var listaContatos: MutableList<User>, private val context: Context
) :
    RecyclerView.Adapter<ContatosAdapter.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_contatos, parent, false)
        return MyViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contato = listaContatos[position]
        val cabecalho: Boolean = contato.email.isEmpty()
        holder.binding.textEmailContato.setText(contato.email)
        holder.binding.textNomeContato.setText(contato.nome)

        if (!contato.foto.equals("")) {
            val uri = Uri.parse(contato.foto)
            Glide.with(context).load(uri).into(holder.binding.imageViewFotoContato)
        } else {
            if (cabecalho) {
                holder.binding.imageViewFotoContato.setImageResource(R.drawable.icone_grupo)
                holder.binding.textEmailContato.setVisibility(View.GONE)
            } else {
                holder.binding.imageViewFotoContato.setImageResource(R.drawable.padrao)
            }
        }
    }

    override fun getItemCount(): Int {
        return listaContatos.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding: AdapterContatosBinding by lazy { AdapterContatosBinding.bind(itemView) }
    }

}