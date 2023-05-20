package com.example.liltoys;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {

    EditText emailPass;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        emailPass = (EditText)findViewById(R.id.emailPass);


        String email = emailPass.getText().toString();
        Button send  = (Button)findViewById(R.id.btnReset);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(emailPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            //Log.d(TAG, "Email recovery sent");
                            Toast.makeText(getApplicationContext(), "Email recovery sent! Please check your email.",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            startActivity(i);
                        }
                    }
                });
            }
        });


    }
}