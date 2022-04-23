package com.gzuazo.myapplication.ActualizarNota;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gzuazo.myapplication.R;

public class Actualizar_Nota extends AppCompatActivity {

    TextView id_nota_act, uid_usuario_act, correo_usuario_act, fecha_hora_registro_act, fecha_act, estado_act, estado_nuevo;
    EditText titulo_nota_act, descripcion_act;
    Button  btn_Calendario_act;

    ImageView tareaFinalizada, tareaNoFinalizada;

    Spinner spinnerEstado;

    // Strings para almacenar/recoger los datos de la actividad anterior
    String id_nota, uid_usuario, correo_usuario, fecha_hora_registro, fecha, estado, tituloNota, descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_nota);
        inicializarViews();
        getDatosIntent();
        setterDatos();
        checkEstadoNota();
        spinner_estado();
    }

    private void inicializarViews(){
        id_nota_act = findViewById(R.id.Id_nota_Actualizar);
        uid_usuario_act = findViewById(R.id.Uid_Usuario_Actualizar);
        correo_usuario_act = findViewById(R.id.Correo_Usuario_Actualizar);
        fecha_hora_registro_act = findViewById(R.id.Fecha_Hora_registro_Actualizar);
        titulo_nota_act = findViewById(R.id.Titulo_Nota_Actualizar);
        descripcion_act = findViewById(R.id.Descripcion_Actualizar);
        btn_Calendario_act = findViewById(R.id.btn_Calendario_Actualizar);
        fecha_act = findViewById(R.id.Fecha_Actualizar);
        estado_act = findViewById(R.id.Estado_Actualizar);
        tareaFinalizada = findViewById(R.id.Tarea_Finalizada);
        tareaNoFinalizada = findViewById(R.id.Tarea_No_Finalizada);
        spinnerEstado = findViewById(R.id.Spinner_estado);
        estado_nuevo = findViewById(R.id.Estado_Nuevo);
    }

    private void getDatosIntent(){
        Bundle intent = getIntent().getExtras();
        id_nota = intent.getString("id_Nota"); uid_usuario = intent.getString("uid_usuario");
        correo_usuario = intent.getString("correo_usuario");
        fecha_hora_registro = intent.getString("fecha_registrada");
        fecha = intent.getString("fechaNota");
        estado = intent.getString("estado");
        tituloNota = intent.getString("tituloNota");
        descripcion = intent.getString("descripcion");
    }

    private void setterDatos(){
        id_nota_act.setText(id_nota);
        uid_usuario_act.setText(uid_usuario);
        correo_usuario_act.setText(correo_usuario);
        fecha_hora_registro_act.setText(fecha_hora_registro);
        fecha_act.setText(fecha);
        estado_act.setText(estado);
        titulo_nota_act.setText(tituloNota);
        descripcion_act.setText(descripcion);
    }

    private void checkEstadoNota(){
        String estado_nota = estado_act.getText().toString();

        if (estado_nota.equals("No finalizado")){
            tareaNoFinalizada.setVisibility(View.VISIBLE);
        }
        if (estado_nota.equals("Finalizado")){
            tareaFinalizada.setVisibility(View.VISIBLE);
        }
    }

    private void spinner_estado(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Estados_Nota, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);
    }
}