package com.example.liltoys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import android.window.SplashScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Splashscreen extends AppCompatActivity {

    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();


        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
                String email = prefs.getString("pemail", null);
                String password = prefs.getString("ppassword", null);

                if(email != null && password != null){
                    Intent i = new Intent(Splashscreen.this, MainActivity.class);
                    Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(Splashscreen.this, Onboarding.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 3000);

    }
}