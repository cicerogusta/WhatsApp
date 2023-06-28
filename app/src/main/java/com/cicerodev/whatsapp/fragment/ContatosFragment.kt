package com.cicerodev.whatsapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cicerodev.whatsapp.activity.ChatActivity
import com.cicerodev.whatsapp.activity.GrupoActivity
import com.cicerodev.whatsapp.adapter.ContatosAdapter
import com.cicerodev.whatsapp.databinding.FragmentContatosBinding
import com.cicerodev.whatsapp.helper.RecyclerItemClickListener
import com.cicerodev.whatsapp.model.Usuario
import com.google.firebase.auth.FirebaseUser

/**
 * A simple [Fragment] subclass.
 */
class ContatosFragment : Fragment() {
    private var adapter: ContatosAdapter? = null
    private var listaContatos: MutableList<Usuario> = ArrayList<Usuario>()
    private var usuarioAtual: FirebaseUser? = null
    private lateinit var binding: FragmentContatosBinding
    private val contatosViewModel: ContatosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContatosBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        val view: View = binding.root

        contatosViewModel.recuperarContatos().observe(this) {
            if (it.isNotEmpty()) {
               listaContatos = it

            }
            if (listaContatos.isEmpty()) {
                Toast.makeText(requireContext(), "VAZIO", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
                adapter = activity?.let { activity -> ContatosAdapter(listaContatos, activity) }
                //configurar recyclerview
                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
                binding.recyclerViewListaContatos.setLayoutManager(layoutManager)
                binding.recyclerViewListaContatos.setHasFixedSize(true)
                binding.recyclerViewListaContatos.setAdapter(adapter)

                //Configurar evento de clique no recyclerview
                binding.recyclerViewListaContatos.addOnItemTouchListener(
                    RecyclerItemClickListener(
                        activity,
                        binding.recyclerViewListaContatos,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View?, position: Int) {
                                val listaUsuariosAtualizada: List<Usuario>? = adapter?.contatos

                                val usuarioSelecionado = listaUsuariosAtualizada?.get(position)
                                val cabecalho: Boolean = usuarioSelecionado?.email?.isEmpty() == true

                                if (cabecalho) {
                                    val i = Intent(activity, GrupoActivity::class.java)
                                    startActivity(i)
                                } else {
                                    val i = Intent(activity, ChatActivity::class.java)
                                    i.putExtra("chatContato", usuarioSelecionado)
                                    startActivity(i)
                                }
                            }

                            override fun onItemClick(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                            }

                            override fun onLongItemClick(view: View?, position: Int) {
                            }

                        }
                    )
                )





            }

        }

        //Configurações iniciais
        usuarioAtual = contatosViewModel.getUsuarioAtual()

        //configurar adapter



        return view
    }

    override fun onStart() {
        super.onStart()
        contatosViewModel.recuperarContatos()
    }

    override fun onStop() {
        super.onStop()
    }
}