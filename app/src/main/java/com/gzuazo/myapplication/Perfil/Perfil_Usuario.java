package com.gzuazo.myapplication.Perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gzuazo.myapplication.MenuPrincipal;
import com.gzuazo.myapplication.R;

public class Perfil_Usuario extends AppCompatActivity {

    ImageView imagen_perfil;
    TextView correo_Perfil, uid_Perfil;
    EditText nombre_Perfil, apellidos_Perfil, edad_Perfil, telefono_Perfil, domicilio_Perfil;
    Button btn_Save;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        inicializarViews();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
    }

    private void inicializarViews() {
        imagen_perfil = findViewById(R.id.imagen_perfil);
        correo_Perfil = findViewById(R.id.Correo_Perfil);
        uid_Perfil = findViewById(R.id.Uid_perfil);
        nombre_Perfil = findViewById(R.id.Nombre_Perfil);
        apellidos_Perfil = findViewById(R.id.Apellidos_Perfil);
        edad_Perfil = findViewById(R.id.Edad_Perfil);
        telefono_Perfil = findViewById(R.id.Telefono_Perfil);
        domicilio_Perfil = findViewById(R.id.Domicilio_Perfil);
        btn_Save = findViewById(R.id.GuardarDatos_Perfil);
    }

    private void lecturaDatos(){
        usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    // si el usuario existe
                    //Obtener datos
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombre = ""+snapshot.child("nombre").getValue();
                    String apellidos = ""+snapshot.child("apellidos").getValue();
                    String telefono = ""+snapshot.child("telefono").getValue();
                    String correo = ""+snapshot.child("correo").getValue();
                    String domicilio = ""+snapshot.child("domicilio").getValue();
                    String edad = ""+snapshot.child("edad").getValue();

                    // Settear los datos
                    Toast.makeText(Perfil_Usuario.this, uid, Toast.LENGTH_SHORT).show();
                    uid_Perfil.setText(uid);
                    nombre_Perfil.setText(nombre);
                    apellidos_Perfil.setText(apellidos);
                    telefono_Perfil.setText(telefono);
                    correo_Perfil.setText(correo);
                    domicilio_Perfil.setText(domicilio);
                    edad_Perfil.setText(edad);
                }else{
                    Toast.makeText(Perfil_Usuario.this, "Esperando datos...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Perfil_Usuario.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validarInicioSesion(){
        if(user != null){
            lecturaDatos();
        }else {
            startActivity(new Intent(Perfil_Usuario.this, MenuPrincipal.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        validarInicioSesion();
        super.onStart();

    }
}