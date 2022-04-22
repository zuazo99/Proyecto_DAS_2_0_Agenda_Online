package com.gzuazo.myapplication.ListarNotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gzuazo.myapplication.R;
import com.gzuazo.myapplication.adapter.ViewHolder_nota;
import com.gzuazo.myapplication.models.Nota;

public class ListarNotas extends AppCompatActivity {

    RecyclerView mRecycler;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference database;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Nota, ViewHolder_nota> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Nota> options;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_notas);

        // Configurar el ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.tituloActionBarListarNotas));
        // Acciones para crear la flecha hacia atras
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mRecycler = findViewById(R.id.recyclerviewNotas);
        mRecycler.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        database = firebaseDatabase.getReference("Notas_Publicadas");
        dialog = new Dialog(ListarNotas.this);
        listNotesOfUsers();

    }

    private void listNotesOfUsers(){
        options = new FirebaseRecyclerOptions.Builder<Nota>().setQuery(database, Nota.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nota, ViewHolder_nota>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_nota viewHolder_nota, int position, @NonNull Nota nota) {
                viewHolder_nota.setterDatos(
                        getApplicationContext(), nota.getId_nota(),
                        nota.getUid_usuario(), nota.getCorreo_usuario(),
                        nota.getFechaHoraActual(), nota.getTituloNota(),
                        nota.getDescripcion(), nota.getFechaNota(),
                        nota.getEstado()
                );
            }

            @Override
            public ViewHolder_nota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
                ViewHolder_nota viewHolder_nota = new ViewHolder_nota(view);
                viewHolder_nota.setOnClickListener(new ViewHolder_nota.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(ListarNotas.this, "OnItemClick", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        //Toast.makeText(ListarNotas.this, "OnItemLongClick", Toast.LENGTH_SHORT).show();

                        //Declarar las vistas del dialog
                        Button btn_Eliminar, btn_Actualizar;

                        //Realizar conexion con el dise;o
                        dialog.setContentView(R.layout.dialogo_opciones);

                        //Inicializar las vistas
                        btn_Eliminar = dialog.findViewById(R.id.CD_Eliminar);
                        btn_Actualizar = dialog.findViewById(R.id.CD_Actualizar);

                        btn_Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ListarNotas.this, "Nota eliminada", Toast.LENGTH_SHORT).show();
                            }
                        });

                        btn_Actualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ListarNotas.this, "Actualizar nota", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog.show();

                    }
                });
                return viewHolder_nota;
            }
        };

        linearLayoutManager = new LinearLayoutManager(ListarNotas.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true); // se liste del ultimo al primero
        linearLayoutManager.setStackFromEnd(true);

        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}