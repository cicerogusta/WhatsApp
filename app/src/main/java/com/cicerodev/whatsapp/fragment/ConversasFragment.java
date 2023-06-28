package com.cicerodev.whatsapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cicerodev.whatsapp.R;
import com.cicerodev.whatsapp.adapter.ConversasAdapter;
import com.cicerodev.whatsapp.model.Conversa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewConversas;
    private List<Conversa> listaConversas = new ArrayList<>();
    private ConversasAdapter adapter;
    private DatabaseReference database;
    private DatabaseReference conversasRef;
    private ChildEventListener childEventListenerConversas;

    public ConversasFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

//        recyclerViewConversas = view.findViewById(R.id.recyclerListaConversas);
//
//        //Configurar adapter
//        adapter = new ConversasAdapter(listaConversas, getActivity());
//
//        //Configurar recyclerview
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerViewConversas.setLayoutManager( layoutManager );
//        recyclerViewConversas.setHasFixedSize(true);
//        recyclerViewConversas.setAdapter( adapter );
//
//        //Configurar evento de clique
//        recyclerViewConversas.addOnItemTouchListener(
//                new RecyclerItemClickListener(
//                        getActivity(),
//                        recyclerViewConversas,
//                        new RecyclerItemClickListener.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//
//                                List<Conversa> listaConversasAtualizada = adapter.getConversas();
//                                Conversa conversaSelecionada = listaConversasAtualizada.get( position );
//
//                                if( conversaSelecionada.getIsGroup().equals("true") ){
//
//                                    Intent i = new Intent(getActivity(), ChatActivity.class);
//                                    i.putExtra("chatGrupo", conversaSelecionada.getGrupo() );
//                                    startActivity( i );
//
//                                }else {
//                                    Intent i = new Intent(getActivity(), ChatActivity.class);
//                                    i.putExtra("chatContato", conversaSelecionada.getUsuarioExibicao() );
//                                    startActivity( i );
//                                }
//
//                            }
//
//                            @Override
//                            public void onLongItemClick(View view, int position) {
//
//                            }
//
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            }
//                        }
//                )
//        );
//
//
//        //Configura conversas ref
//        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
//        database = ConfiguracaoFirebase.getFirebaseDatabase();
//        conversasRef = database.child("conversas")
//                .child( identificadorUsuario );

        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        recuperarConversas();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        conversasRef.removeEventListener( childEventListenerConversas );
//    }
//
//    public void recarregarConversas(){
//        adapter = new ConversasAdapter(listaConversas, getActivity());
//        recyclerViewConversas.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }
//
//    public void recuperarConversas(){
//
//        childEventListenerConversas = conversasRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                // Recuperar conversas
//                Conversa conversa = dataSnapshot.getValue( Conversa.class );
//                listaConversas.add( conversa );
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

}
