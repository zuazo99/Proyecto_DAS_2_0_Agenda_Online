package com.gzuazo.myapplication.Perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gzuazo.myapplication.ActualizarNota.Actualizar_Nota;
import com.gzuazo.myapplication.MenuPrincipal;
import com.gzuazo.myapplication.R;

import java.util.Calendar;
import java.util.HashMap;

public class Perfil_Usuario extends AppCompatActivity {

    ImageView imagen_perfil, editar_Fecha;
    TextView correo_Perfil, uid_Perfil, fecha_Nacimiento;
    EditText nombre_Perfil, apellidos_Perfil, edad_Perfil, telefono_Perfil, domicilio_Perfil;
    Button btn_Save;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference usuarios;

    int dia, mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        inicializarViews();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        editar_Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendar();
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePerfil();
            }
        });
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
        editar_Fecha = findViewById(R.id.Editar_Fecha);
        fecha_Nacimiento = findViewById(R.id.FechaNacimiento);
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
                    String fecha_nacimiento = ""+ snapshot.child("fecha_nacimiento").getValue();
                    String imagen_perfil =  ""+snapshot.child("imagen_perfil").getValue();

                    // Settear los datos
                    uid_Perfil.setText(uid);
                    nombre_Perfil.setText(nombre);
                    apellidos_Perfil.setText(apellidos);
                    telefono_Perfil.setText(telefono);
                    correo_Perfil.setText(correo);
                    domicilio_Perfil.setText(domicilio);
                    edad_Perfil.setText(edad);
                    fecha_Nacimiento.setText(fecha_nacimiento);

                    loadImagen(imagen_perfil);
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

    private void loadImagen(String imagen) {

        try {
            Glide.with(getApplicationContext()).load(imagen).placeholder(R.drawable.imagen_perfil).into(imagen_perfil);
        }catch (Exception e){
            Glide.with(getApplicationContext()).load(R.drawable.imagen_perfil).into(imagen_perfil);
        }
    }

    private void openCalendar(){
        final Calendar calendario = Calendar.getInstance();

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        anio = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Perfil_Usuario.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int AnioSeleccion, int MesSeleccion, int DiaSeleccion) {
                // DatePickerDialog no devuelve el dia y el mes en un formato adecuado, tenemos que adecuarlo nosotros
                // Para ello, en primer lugar declaramos dos Strings vacios:
                String diaFormatted, mesFormatted;

                // 2. Obtener dia
                if (DiaSeleccion < 10){
                    diaFormatted = "0" + String.valueOf(DiaSeleccion);
                    // Antes: 9/12/2022 - Ahora 09/12/2022
                }else{
                    diaFormatted = String.valueOf(DiaSeleccion);
                }

                //Obtener el mes
                int Mes = MesSeleccion + 1;

                if (MesSeleccion < 10){
                    mesFormatted = "0" + String.valueOf(Mes);
                }else{
                    mesFormatted = String.valueOf(Mes);
                }
                // Seteamos la fecha en el TExtView
                fecha_Nacimiento.setText(diaFormatted + "/" + mesFormatted + "/" + AnioSeleccion);
            }
        }
                ,anio, mes, dia);
        datePickerDialog.show();
    }

    private void updatePerfil(){
        String A_Nombre = nombre_Perfil.getText().toString().trim();
        String A_Apellidos = apellidos_Perfil.getText().toString().trim();
        String A_Edad = edad_Perfil.getText().toString().trim();
        String A_Telefono = telefono_Perfil.getText().toString().trim();
        String A_Domicilio = domicilio_Perfil.getText().toString();
        String A_Fecha_N = fecha_Nacimiento.getText().toString();

        HashMap<String, Object> Datos_Actualizar = new HashMap<>();

        Datos_Actualizar.put("nombres", A_Nombre);
        Datos_Actualizar.put("apellidos", A_Apellidos);
        Datos_Actualizar.put("edad", A_Edad);
        Datos_Actualizar.put("telefono", A_Telefono);
        Datos_Actualizar.put("domicilio", A_Domicilio);
        Datos_Actualizar.put("fecha_de_nacimiento", A_Fecha_N);

        usuarios.child(user.getUid()).updateChildren(Datos_Actualizar)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Perfil_Usuario.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Perfil_Usuario.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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