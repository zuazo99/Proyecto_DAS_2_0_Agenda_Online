package com.gzuazo.myapplication.AgregarNota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gzuazo.myapplication.R;
import com.gzuazo.myapplication.models.Nota;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class AgregarNota extends AppCompatActivity {

    TextView id_Usuario, correo_Usuario, fechaHoraActual, fecha, estado;
    EditText tituloNota, descripcion;
    Button btn_Calendario;

    int dia, mes, anio;

    DatabaseReference firebaseDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nota);

        inicializarActionBar();
        inicializarViews();
        firebaseDB();
        getDatosUsuario();
        getFechaHoraActual();

        btn_Calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();

                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                anio = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AgregarNota.this, new DatePickerDialog.OnDateSetListener() {
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
                        fecha.setText(diaFormatted + "/" + mesFormatted + "/" + AnioSeleccion);
                    }
                }
                ,anio, mes, dia);
                datePickerDialog.show();
            }
        });
    }
    private void inicializarActionBar(){
        //ActionBar para que el usuario pueda volver atras, mediante una flecha
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        // Estos dos metodos crean la flecha
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    private void firebaseDB() {
        firebaseDB = FirebaseDatabase.getInstance().getReference();
    }

    private void getDatosUsuario(){
        String uid_usuario = getIntent().getStringExtra("uid");
        String correo = getIntent().getStringExtra("correo");

        id_Usuario.setText(uid_usuario);
        correo_Usuario.setText(correo);
    }

    private void getFechaHoraActual(){
        String fechaHora = new SimpleDateFormat("dd-MM-YYYY/HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis());
        fechaHoraActual.setText(fechaHora);
    }

    private void addNota() {
        // Obtener los datos
        String uid_usuario = id_Usuario.getText().toString();
        String correo = correo_Usuario.getText().toString();
        String fecha_Hora_Actual = fechaHoraActual.getText().toString();
        String titleNota = tituloNota.getText().toString();
        String Descripcion = descripcion.getText().toString();
        String fechaNota = fecha.getText().toString();
        String Estado = estado.getText().toString();

        // Validar Datos

        //Validar datos
        if (!uid_usuario.equals("") && !correo.equals("") && !fecha_Hora_Actual.equals("") &&
                !titleNota.equals("") && !Descripcion.equals("") && ! fechaNota.equals("") && !Estado.equals("")){

            Nota nota = new Nota(correo+"/"+fecha_Hora_Actual, uid_usuario, correo, fecha_Hora_Actual,
                    titleNota, Descripcion, fechaNota, Estado);
            /*
                push() --> Crea un registro en blanco en la base de datos, y devuelve la referencia del registro
                getKey() --> Para obtener esa referencia
             */

            String nota_Usuario = firebaseDB.push().getKey();
            // Establecer nombre de la BD
            String NOMBRE_DB = "Notas_Publicadas";

            //1.child(NOMBRE_DB) --> Establecemos que la BD "Notas_Publicadas estara dentro de la base de datos general(Padre)"
            firebaseDB.child(NOMBRE_DB).child(nota_Usuario).setValue(nota);
            Toast.makeText(this, getString(R.string.agregarNotaExito), Toast.LENGTH_SHORT).show();
            onBackPressed(); // Para que despues de agregar una nota nos redirija automaticamente al menu principal

        }else {
            Toast.makeText(this, getString(R.string.validarDatosError), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agenda_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.AgregarNota_BD:
                addNota();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Permitira al usuario ir a la actividad anterior cuando presione en la flecha creada en el actionBar
        return super.onSupportNavigateUp();
    }
}