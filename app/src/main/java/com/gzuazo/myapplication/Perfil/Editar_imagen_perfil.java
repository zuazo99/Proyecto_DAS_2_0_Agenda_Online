package com.gzuazo.myapplication.Perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gzuazo.myapplication.R;

public class Editar_imagen_perfil extends AppCompatActivity {

    ImageView imagenPerfilAct;
    Button btn_elegirImagen, btn_ActualizarImage;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Dialog dialog_elegir_imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_imagen_perfil);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Editar imagen de Usuario");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        inicializarViews();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        dialog_elegir_imagen = new Dialog(Editar_imagen_perfil.this);

        btn_elegirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elegirImagen();
            }
        });

        btn_ActualizarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        lecturaImagen();
    }



    private void inicializarViews() {
        imagenPerfilAct = findViewById(R.id.ImagenPerfilActualizar);
        btn_elegirImagen = findViewById(R.id.btnElegirImagen);
        btn_ActualizarImage = findViewById(R.id.btnActualizarImagen);
    }

    private void lecturaImagen(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Obtener el dato de la imagen
                String imagenPerfil = ""+ snapshot.child("imagen_perfil").getValue();
                Glide.with(getApplicationContext())
                        .load(imagenPerfil)
                        .placeholder(R.drawable.imagen_perfil)
                        .into(imagenPerfilAct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void elegirImagen() {
        Button btnGaleria, btnCamara;
        dialog_elegir_imagen.setContentView(R.layout.cuadro_dialogo_elegir_imagen);
        btnGaleria = dialog_elegir_imagen.findViewById(R.id.Btn_Elegir_Galeria);
        btnCamara = dialog_elegir_imagen.findViewById(R.id.Btn_Elegir_Camara);

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Editar_imagen_perfil.this, "Elegir de galería", Toast.LENGTH_SHORT).show();
                dialog_elegir_imagen.dismiss();
            }
        });

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Editar_imagen_perfil.this, "Elegir de cámara", Toast.LENGTH_SHORT).show();
                dialog_elegir_imagen.dismiss();
            }
        });

        dialog_elegir_imagen.show();
        dialog_elegir_imagen.setCanceledOnTouchOutside(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}