package com.gzuazo.myapplication.Perfil;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gzuazo.myapplication.R;

import java.util.HashMap;

public class Editar_imagen_perfil extends AppCompatActivity {

    ImageView imagenPerfilAct;
    Button btn_elegirImagen, btn_ActualizarImage;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Dialog dialog_elegir_imagen;

    Uri imagenUri = null;

    ProgressDialog progressDialog;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_imagen_perfil);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Editar imagen de Usuario");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        inicializarViews();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        dialog_elegir_imagen = new Dialog(Editar_imagen_perfil.this);

        btn_elegirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elegirImagen();
            }
        });

        btn_ActualizarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagenUri == null){
                    Toast.makeText(Editar_imagen_perfil.this, "Inserte una nueva imagen", Toast.LENGTH_SHORT).show();
                }else {
                    subirImagenStorage();
                }
            }
        });

        progressDialog = new ProgressDialog(Editar_imagen_perfil.this);
        progressDialog.setTitle("Espere por favor...");
        progressDialog.setCanceledOnTouchOutside(false);
        lecturaImagen();
    }



    private void inicializarViews() {
        imagenPerfilAct = findViewById(R.id.ImagenPerfilActualizar);
        btn_elegirImagen = findViewById(R.id.btnElegirImagen);
        btn_ActualizarImage = findViewById(R.id.btnActualizarImagen);
    }

    private void lecturaImagen(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Obtener el dato de la imagen
                String imagenPerfil = ""+ snapshot.child("imagen_perfil").getValue();
                Glide.with(getApplicationContext())
                        .load(imagenPerfil)
                        .placeholder(R.drawable.imagen_perfil)
                        .into(imagenPerfilAct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void subirImagenStorage(){
        progressDialog.setMessage("Subiendo imagen...");
        progressDialog.show();
        String carpetaImagenes = "ImagenesPerfil/"; // Aqui almacenamos todas las imagenes de los usuarios
        String nombreImagen = carpetaImagenes+firebaseAuth.getUid();

        StorageReference reference = FirebaseStorage.getInstance().getReference(nombreImagen);
        reference.putFile(imagenUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uriImagen = ""+uriTask.getResult(); // Obtenemos la uri que se ha subido al Storage
                        // Enviamos la uri a la base de datos
                        updateImagenBD(uriImagen);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Editar_imagen_perfil.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateImagenBD(String uriImagen) {
        progressDialog.setMessage("Actualizando la imagen");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        if (imagenUri != null){
            hashMap.put("imagen_perfil", ""+uriImagen);
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(user.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Editar_imagen_perfil.this, "Imagen se ha actualizado con éxito", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Editar_imagen_perfil.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void elegirImagen() {
        Button btnGaleria, btnCamara;
        dialog_elegir_imagen.setContentView(R.layout.cuadro_dialogo_elegir_imagen);
        btnGaleria = dialog_elegir_imagen.findViewById(R.id.Btn_Elegir_Galeria);
        btnCamara = dialog_elegir_imagen.findViewById(R.id.Btn_Elegir_Camara);

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Editar_imagen_perfil.this, "Elegir de galería", Toast.LENGTH_SHORT).show();
                //seleccionImagenGaleria();
                if (ContextCompat.checkSelfPermission(Editar_imagen_perfil.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    seleccionImagenGaleria();
                    dialog_elegir_imagen.dismiss();
                }else{
                    solicitudPermisosGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    dialog_elegir_imagen.dismiss();
                }

            }
        });

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Editar_imagen_perfil.this, "Elegir de cámara", Toast.LENGTH_SHORT).show();
               // seleccionarImagenCamara();
               // dialog_elegir_imagen.dismiss();
                if (ContextCompat.checkSelfPermission(Editar_imagen_perfil.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    seleccionarImagenCamara();
                    dialog_elegir_imagen.dismiss();
                }else {
                    solicitudPermisosCamara.launch(Manifest.permission.CAMERA);
                    dialog_elegir_imagen.dismiss();
                }
            }
        });

        dialog_elegir_imagen.show();
        dialog_elegir_imagen.setCanceledOnTouchOutside(true);
    }



    private void seleccionImagenGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galeriaActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galeriaActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Gestionamos el resultado de nuestro intent
                    if (result.getResultCode() == Activity.RESULT_OK){
                        // Obtener URI de la imagen
                        Intent data = result.getData();
                        imagenUri = data.getData();

                        // Settear l aimagen seleccionada
                        imagenPerfilAct.setImageURI(imagenUri);
                    }else {
                        Toast.makeText(Editar_imagen_perfil.this, "Cancelado por el Usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    /* Permiso para acceder a la galeria*/
    private ActivityResultLauncher<String> solicitudPermisosGaleria =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if(isGranted){
                    // isGranted --> Es concedido el permiso
                    seleccionImagenGaleria();
                }else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });

    private void seleccionarImagenCamara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nueva imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripcion de imagen");
        // Estamos creando el image URI
        imagenUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);
        camaraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> camaraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        imagenPerfilAct.setImageURI(imagenUri);
                    }else{
                        Toast.makeText(Editar_imagen_perfil.this, "Cancelado por el Usuario", Toast.LENGTH_SHORT).show();

                    }
                }
            }
    );

    /* Permiso para acceder a la camara*/
    private ActivityResultLauncher<String> solicitudPermisosCamara =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if (isGranted){
                    seleccionarImagenCamara();
                }else {
                    Toast.makeText(this, "Permiso denegado.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}