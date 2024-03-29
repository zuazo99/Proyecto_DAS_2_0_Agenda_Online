package com.gzuazo.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gzuazo.myapplication.AgregarNota.AgregarNota;
import com.gzuazo.myapplication.ListarNotas.ListarNotas;
import com.gzuazo.myapplication.NotasArchivadas.NotasArchivadas;
import com.gzuazo.myapplication.Perfil.Perfil_Usuario;
import com.gzuazo.myapplication.notificacion.PushNotificationService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuPrincipal extends AppCompatActivity {

    TextView nombresPrincipal, correoPrincipal, idPrincipal;
    Button btnCerrarSesion, btnAgregarNota, btnMisNotas, btnArchivar, btnContacto, btnAcercaDe, btnEstadoCuenta;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseAnalytics mFirebaseAnalytics;
    ProgressBar progressBarDatos;
    ProgressDialog progressDialog;
    LinearLayoutCompat linearNombres, linearCorreo, linearVerificacion;

    DatabaseReference usuarios; // Para leer o escribir en la base de datos

    PushNotificationService notificacion;

    String direccion = "http://http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/gzuazo004/WEB/notify.php";

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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        btnEstadoCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()){
                    // si la cuenta esta verificada
                    Toast.makeText(MenuPrincipal.this, "Cuenta verificada", Toast.LENGTH_SHORT).show();
                }else {
                    verificarCuentaCorreo();
                }
            }
        });

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

        btnContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(MenuPrincipal.this, Perfil_Usuario.class));
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

    private void verificarCuentaCorreo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verificar Cuenta");
        builder.setMessage("¿Estas seguro de enviar instrucciones de verificacion a su correo electronico? "
        + user.getEmail())
        .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enviarVerificacion();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MenuPrincipal.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        }).show();

    }

    private void enviarVerificacion() {
        progressDialog.setMessage("Enviando las instrucciones de verificacion a su correo electronico "+
                user.getEmail());
        progressDialog.show();
        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Envio exitoso
                        progressDialog.dismiss();
                        Toast.makeText(MenuPrincipal.this, "Instrucciones enviadas...mire su bandeja de entrada " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MenuPrincipal.this, "Error --> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkEstadoCuenta(){
        String Verificado = "Verificado";
        String No_Verificado = "No Verificado";

        if (user.isEmailVerified()){
            btnEstadoCuenta.setText(Verificado);
            btnEstadoCuenta.setBackgroundColor(Color.rgb(46, 134, 193));
        }else{
            btnEstadoCuenta.setText(No_Verificado);
        }
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

        checkEstadoCuenta();
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
                    linearVerificacion.setVisibility(View.VISIBLE);

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
                    btnContacto.setEnabled(true);
                    btnAcercaDe.setEnabled(true);
                    btnCerrarSesion.setEnabled(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getTokenFCM();
    }
    private void getTokenFCM(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MenuPrincipal.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String token = task.getResult();
                        System.out.println("TOKEN--> "+ token);
                        Toast.makeText(MenuPrincipal.this, "TOKEN --> " + token, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Perfil_Usuario){
            startActivity(new Intent(MenuPrincipal.this, Perfil_Usuario.class));
        }
        return super.onOptionsItemSelected(item);
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
        btnContacto = findViewById(R.id.Contactos);
        progressBarDatos = findViewById(R.id.progressBarDatos);
        linearNombres = findViewById(R.id.Linear_Nombre);
        linearCorreo = findViewById(R.id.Linear_Correo);
        linearVerificacion = findViewById(R.id.Linear_Verificar);
        btnEstadoCuenta = findViewById(R.id.EstadoCuentaPrincipal);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void httpConnection(){
        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}