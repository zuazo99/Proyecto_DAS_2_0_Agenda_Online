package com.gzuazo.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoadScreen extends AppCompatActivity {


    // Comprobar si el usuario tiene la sesion iniciada
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        firebaseAuth = FirebaseAuth.getInstance();

        int Tiempo = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*startActivity(new Intent(LoadScreen.this, MainActivity.class));
                finish();*/
                verificarUsuario();
            }
        }, Tiempo);
    }

    private void verificarUsuario(){
        FirebaseUser user = firebaseAuth.getCurrentUser(); // Estamos obteniendo el usuario actual(previamente se registro y inicio sesion)
        if (user == null){
            startActivity(new Intent(LoadScreen.this, MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(LoadScreen.this, MenuPrincipal.class));
        }
    }
}