package com.gzuazo.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

public class MenuPrincipal extends AppCompatActivity {

    TextView nombresPrincipal, correoPrincipal;
    Button btnCerrarSesion;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ProgressBar progressBarDatos;

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
                    nombresPrincipal.setVisibility(View.VISIBLE);
                    correoPrincipal.setVisibility(View.VISIBLE);

                    // Obtener datos

                    String nombres = ""+ snapshot.child("nombre").getValue();
                    String correo = ""+ snapshot.child("correo").getValue();

                    // settear datos en los textViews
                    nombresPrincipal.setText(nombres);
                    correoPrincipal.setText(correo);
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
        btnCerrarSesion = findViewById(R.id.CerrarSesion);
        progressBarDatos = findViewById(R.id.progressBarDatos);
    }
}