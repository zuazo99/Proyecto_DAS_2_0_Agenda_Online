package com.gzuazo.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

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
            }
        });

        TengounacuentaTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        }
        else{
            crearCuenta();
        }
    }

    private void crearCuenta() {

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