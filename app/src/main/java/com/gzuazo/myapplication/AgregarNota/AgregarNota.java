package com.gzuazo.myapplication.AgregarNota;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gzuazo.myapplication.R;

public class AgregarNota extends AppCompatActivity {

    TextView id_Usuario, correo_Usuario, fechaHoraActual, fecha, estado;
    EditText tituloNota, descripcion;
    Button btn_Calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nota);


        //ActionBar para que el usuario pueda volver atras, mediante una flecha
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        // Estos dos metodos crean la flecha
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        inicializarViews();
        getDatosUsuario();
    }

    private void inicializarViews() {
        id_Usuario = findViewById(R.id.Uid_Usuario);
        correo_Usuario = findViewById(R.id.Correo_Usuario);
        fechaHoraActual = findViewById(R.id.Fecha_Hora_Actual);
        tituloNota = findViewById(R.id.Titulo_Nota);
        descripcion = findViewById(R.id.Descripcion);
        btn_Calendario = findViewById(R.id.btn_Calendario);
        fecha = findViewById(R.id.Fecha);
        estado = findViewById(R.id.Estado);
    }

    private void getDatosUsuario(){
        String uid_usuario = getIntent().getStringExtra("uid");
        String correo = getIntent().getStringExtra("correo");

        id_Usuario.setText(uid_usuario);
        correo_Usuario.setText(correo);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Permitira al usuario ir a la actividad anterior cuando presione en la flecha creada en el actionBar
        return super.onSupportNavigateUp();
    }
}