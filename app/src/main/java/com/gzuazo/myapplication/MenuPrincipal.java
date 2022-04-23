package com.gzuazo.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gzuazo.myapplication.AgregarNota.AgregarNota;
import com.gzuazo.myapplication.ListarNotas.ListarNotas;
import com.gzuazo.myapplication.NotasArchivadas.NotasArchivadas;
import com.gzuazo.myapplication.Perfil.Perfil_Usuario;

public class MenuPrincipal extends AppCompatActivity {

    TextView nombresPrincipal, correoPrincipal, idPrincipal;
    Button btnCerrarSesion, btnAgregarNota, btnMisNotas, btnArchivar, btnPerfil, btnAcercaDe;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ProgressBar progressBarDatos;
    LinearLayoutCompat linearNombres, linearCorreo;

    DatabaseReference usuarios; // Para leer o escribir en la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.tituloMenuPrincipalActionBar));

        inicializarViews();

        //Inicializar Firebase
        usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        btnAgregarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid_usuario = idPrincipal.getText().toString(), correo_usuario = correoPrincipal.getText().toString();
                Intent intent = new Intent(MenuPrincipal.this, AgregarNota.class);
                intent.putExtra("uid", uid_usuario);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);

            }
        });
        btnMisNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, ListarNotas.class));
            }
        });

        btnArchivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, NotasArchivadas.class));
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Perfil_Usuario.class));
            }
        });

        btnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuPrincipal.this, "Acerca De", Toast.LENGTH_SHORT).show();
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salirApp();
            }
        });
    }

    @Override
    protected void onStart() {
        validateInicioSesion();
        super.onStart();
    }

    private void validateInicioSesion(){
        if (user != null){
            // el usuario ha iniciado sesion
            cargarDatos();
        }else{

            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }

    private void cargarDatos(){
        usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // Nos permite leer a tiempo real los datos de ese usuario
                // si el usuario existe
                if(snapshot.exists()){
                    // progressBar se oculta
                    progressBarDatos.setVisibility(View.GONE);
                    // textviews se ven
                    //idPrincipal.setVisibility(View.VISIBLE);
//                    nombresPrincipal.setVisibility(View.VISIBLE);
//                    correoPrincipal.setVisibility(View.VISIBLE);
                    linearNombres.setVisibility(View.VISIBLE);
                    linearCorreo.setVisibility(View.VISIBLE);

                    // Obtener datos
                    String uid = "" + snapshot.child("uid").getValue();
                    String nombres = ""+ snapshot.child("nombre").getValue();
                    String correo = ""+ snapshot.child("correo").getValue();

                    // settear datos en los textViews
                    idPrincipal.setText(uid);
                    nombresPrincipal.setText(nombres);
                    correoPrincipal.setText(correo);


                    // Habilitar botones de opciones

                    btnAgregarNota.setEnabled(true);
                    btnMisNotas.setEnabled(true);
                    btnArchivar.setEnabled(true);
                    btnPerfil.setEnabled(true);
                    btnAcercaDe.setEnabled(true);
                    btnCerrarSesion.setEnabled(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void salirApp() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, getString(R.string.salirApp), Toast.LENGTH_SHORT).show();
    }

    private void inicializarViews(){
        nombresPrincipal = findViewById(R.id.NombresPrincipal);
        correoPrincipal = findViewById(R.id.CorreoPrincipal);
        idPrincipal = findViewById(R.id.IDPrincipal);
        btnCerrarSesion = findViewById(R.id.CerrarSesion);
        btnAcercaDe = findViewById(R.id.AcercaDe);
        btnAgregarNota = findViewById(R.id.AgregarNota);
        btnArchivar = findViewById(R.id.ArchivarNotas);
        btnMisNotas = findViewById(R.id.ListarNotas);
        btnPerfil = findViewById(R.id.Perfil);
        progressBarDatos = findViewById(R.id.progressBarDatos);
        linearNombres = findViewById(R.id.Linear_Nombre);
        linearCorreo = findViewById(R.id.Linear_Correo);
    }
}