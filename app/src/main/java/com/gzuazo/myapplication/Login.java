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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText correoLogin, loginPass;
    Button btn_logeo;
    TextView usuarioNuevoTXT;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    // Validar datos
    String correo = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configurar el ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.titleActionBarLogin));
        // Acciones para crear la flecha hacia atras
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        inicializarViews();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle(getString(R.string.tituloProgressDialog));
        progressDialog.setCanceledOnTouchOutside(false); // La ejecucion del progressDialog no podra cerrarse cuando el usuario presione en cualquier parte de la pantalal

        btn_logeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarLogin();
            }
        });

        usuarioNuevoTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    private void validarLogin() {
        correo = correoLogin.getText().toString();
        password = loginPass.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) { // Valida que el correo contenga un '@' y que acabe en '.com'
            Toast.makeText(this, getString(R.string.toastIngreseCorreo), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getString(R.string.toastIngreseContraseña), Toast.LENGTH_SHORT).show();
        }else{
            loginUsuario();
        }
    }

    private void loginUsuario() {
        progressDialog.setMessage(getString(R.string.messageInicioSesionProgressDialog));
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(Login.this, MenuPrincipal.class));
                            Toast.makeText(Login.this, getString(R.string.welcomeUserMessage) + user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish(); // Por si retrocedemos para que no acceda a la actividad de login
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, getString(R.string.loginFailure), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        // Para que a la hora de pulsar la flecha en el actionbar nos redirija a la actividad anterior
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void inicializarViews() {
        correoLogin = (EditText) findViewById(R.id.CorreoLogin);
        loginPass = findViewById(R.id.LoginPass);
        btn_logeo = findViewById(R.id.btn_logeo);
        usuarioNuevoTXT = findViewById(R.id.UsuarioNuevoTXT);
    }
}