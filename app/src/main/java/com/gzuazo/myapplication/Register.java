package com.gzuazo.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText nombreET, CorreoET, ContraseñaET, ConfirmarContraseñaET;
    Button RegistrarUsuario;
    TextView TengounacuentaTXT;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    // Declarar variables para recoger los datos de los editText

    String nombre = " ", correo = " ", password = " ", confirmarPassword = " ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Configurar el ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.tituloActionBar));
            // Acciones para crear la flecha hacia atras
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Inicializar las vistas
        setViews();

        // Inicializar FireBase Authorization
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle(getString(R.string.tituloProgressDialog));
        progressDialog.setCanceledOnTouchOutside(false); // La ejecucion del progressDialog no podra cerrarse cuando el usuario presione en cualquier parte de la pantalal

        RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                validarDatos();
            }
        });

        TengounacuentaTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }

    private void validarDatos(){
        nombre = nombreET.getText().toString();
        correo = CorreoET.getText().toString();
        password = ContraseñaET.getText().toString();
        confirmarPassword = ConfirmarContraseñaET.getText().toString();

        if (TextUtils.isEmpty(nombre)){
            Toast.makeText(this, getString(R.string.toastIngreseNombre), Toast.LENGTH_SHORT).show();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){ // Valida que el correo contenga un '@' y que acabe en '.com'
            Toast.makeText(this, getString(R.string.toastIngreseCorreo), Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, getString(R.string.toastIngreseContraseña), Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(confirmarPassword)){
            Toast.makeText(this, getString(R.string.toastIngreseConfirmeContraseña), Toast.LENGTH_SHORT).show();
        }else if (!password.equals(confirmarPassword)){
            Toast.makeText(this, getString(R.string.compararPassword), Toast.LENGTH_SHORT).show();
        }
        else{
            crearCuenta();
        }
    }

    private void crearCuenta() {
        progressDialog.setMessage(getString(R.string.messageProgressDialog));
        progressDialog.show();

        // Crear un usuario en FireBase

        firebaseAuth.createUserWithEmailAndPassword(correo, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) { // Permite gestionar el codigo necesario para realizar un registro exitoso
                            // Registro exitoso
                            guardarInformacion();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // aqui entramos si ha ocurrido un error a la hora de crear el usuario
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, getString(R.string.onFailure)+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
        });
    }

    private void guardarInformacion() {
        progressDialog.setMessage(getString(R.string.messageSaveProgressDialog));
        progressDialog.dismiss();

        //Obtener la identificacion del usuario actual
        String uid = firebaseAuth.getUid();

        HashMap<String, String> datos = new HashMap<>();
        datos.put("uid", uid);
        datos.put("correo", correo);
        datos.put("nombre", nombre);
        datos.put("password", password);

        // Obtenemos la referencia de la base de datos
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid)
                .setValue(datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Register.this, getString(R.string.messageCuentaCreada), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, MenuPrincipal.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setViews(){
        nombreET = findViewById(R.id.nombreET);
        CorreoET = findViewById(R.id.CorreoET);
        ContraseñaET = findViewById(R.id.ContraseñaET);
        ConfirmarContraseñaET = findViewById(R.id.ConfirmarContraseñaET);
        RegistrarUsuario = findViewById(R.id.RegistrarUsuario);
        TengounacuentaTXT = findViewById(R.id.TengounacuentaTXT);
    }
}