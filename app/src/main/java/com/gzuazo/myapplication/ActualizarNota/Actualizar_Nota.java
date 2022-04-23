package com.gzuazo.myapplication.ActualizarNota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gzuazo.myapplication.AgregarNota.AgregarNota;
import com.gzuazo.myapplication.R;

import java.util.Calendar;

public class Actualizar_Nota extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView id_nota_act, uid_usuario_act, correo_usuario_act, fecha_hora_registro_act, fecha_act, estado_act, estado_nuevo;
    EditText titulo_nota_act, descripcion_act;
    Button  btn_Calendario_act;

    ImageView tareaFinalizada, tareaNoFinalizada;

    Spinner spinnerEstado;

    int dia, mes, anio;


    // Strings para almacenar/recoger los datos de la actividad anterior
    String id_nota, uid_usuario, correo_usuario, fecha_hora_registro, fecha, estado, tituloNota, descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_nota);
        inicializarActionBar();
        inicializarViews();
        getDatosIntent();
        setterDatos();
        checkEstadoNota();
        spinner_estado();
        btn_Calendario_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate();
            }
        });
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

    private void selectedDate(){
        final Calendar calendario = Calendar.getInstance();

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        anio = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Actualizar_Nota.this, new DatePickerDialog.OnDateSetListener() {
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
                fecha_act.setText(diaFormatted + "/" + mesFormatted + "/" + AnioSeleccion);
            }
        }
                ,anio, mes, dia);
        datePickerDialog.show();
    }

    private void spinner_estado(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Estados_Nota, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);
        spinnerEstado.setOnItemSelectedListener(this);
    }

    private void updateNotaBD(){
        String titleUpdate = titulo_nota_act.getText().toString();
        String descripcionUpdate = descripcion_act.getText().toString();
        String fechaUpdate = fecha_act.getText().toString();
        String estadoUpdate = estado_nuevo.getText().toString();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Notas_Publicadas");

        // Consulta
        Query query = databaseReference.orderByChild("id_nota").equalTo(id_nota);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*
                El for va a recorrer los registros de notas de la base de datos hasta encontrar
                el id_nota equivalente al id de la nota seleccionada por el usuario
                 */
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("tituloNota").setValue(titleUpdate);
                    ds.getRef().child("descripcion").setValue(descripcionUpdate);
                    ds.getRef().child("fechaNota").setValue(fechaUpdate);
                    ds.getRef().child("estado").setValue(estadoUpdate);
                }

                Toast.makeText(Actualizar_Nota.this, getString(R.string.notaActualizadaExito), Toast.LENGTH_SHORT).show();
                onBackPressed(); // Para dirigirnos automaticamente a la actividad anterior
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*
        Estos dos metodos nos sirven para poder seleccionar las opciones del spinner y obtener el valor seleccionado
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String estadoActual = estado_act.getText().toString();

        String posicion_1 = adapterView.getItemAtPosition(1).toString();

        String estado_seleccionado = adapterView.getItemAtPosition(i).toString();
        estado_nuevo.setText(estado_seleccionado);

        if (estadoActual.equals("Finalizado")){
            estado_nuevo.setText(posicion_1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_actualizar_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ActualizarNota_BD:
                updateNotaBD();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inicializarActionBar(){
        //ActionBar para que el usuario pueda volver atras, mediante una flecha
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.actionBarTitleActualizar));
        // Estos dos metodos crean la flecha
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}