package com.cicerodev.whatsappcomdi.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.cicerodev.whatsappcomdi.R
import com.cicerodev.whatsappcomdi.data.model.Mensagem
import com.cicerodev.whatsappcomdi.databinding.AdapterMensagemDestinatarioBinding
import com.cicerodev.whatsappcomdi.databinding.AdapterMensagemRemetenteBinding

class MensagensAdapter(
    private var listaMensagens: MutableList<Mensagem>, private val context: Context, private val idUsuario: String
) :
    RecyclerView.Adapter<MensagensAdapter.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var item: ViewBinding? = null
        if (viewType == TIPO_REMETENTE) {
            item = AdapterMensagemRemetenteBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.adapter_mensagem_remetente, parent, false))




        } else if (viewType == TIPO_DESTINATARIO) {
            item = AdapterMensagemDestinatarioBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.adapter_mensagem_destinatario, parent, false))


        }
        return MyViewHolder(item!!.root)

    }

    private val TIPO_REMETENTE = 1
    private val TIPO_DESTINATARIO = 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val mensagem = listaMensagens[position]
        val msg = mensagem.mensagem
        val imagem = mensagem.imagem
        if (!imagem.equals("")) {
            val url = Uri.parse(imagem)
            Glide.with(context).load(url).into(holder.imagem)
            val nome = mensagem.nome
            if (nome != null) {
                if (!nome.isEmpty()) {
                    holder.nome.text = nome
                } else {
                    holder.nome.visibility = View.GONE
                }
            }

            //Esconder o texto
            holder.mensagem.visibility = View.GONE
        } else {
            holder.mensagem.text = msg
            val nome = mensagem.nome
            if (nome != null) {
                if (!nome.isEmpty()) {
                    holder.nome.text = nome
                } else {
                    holder.nome.visibility = View.GONE
                }
            }

            //Esconder a imagem
            holder.imagem.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return listaMensagens.size
    }

    override fun getItemViewType(position: Int): Int {
        val mensagem = listaMensagens[position]
        if (idUsuario == mensagem.idUsuario) {
            return TIPO_REMETENTE
        }
        return TIPO_DESTINATARIO


    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imagem: ImageView = itemView.rootView.findViewById<ImageView>(R.id.imageMensagemFoto)
        val mensagem: TextView = itemView.rootView.findViewById<TextView>(R.id.textMensagemTexto)
        val nome: TextView = itemView.rootView.findViewById<TextView>(R.id.textNomeMensagem)
    }

}