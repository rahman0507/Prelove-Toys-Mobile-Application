package com.example.liltoys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email, password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=(EditText) findViewById(R.id.inputemail);
        password=(EditText) findViewById(R.id.inputpassword);
        auth=FirebaseAuth.getInstance();
        getSupportActionBar().hide();


        //boleh guna untuk logout
        //SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
        //editor.clear().apply();

        //checkBox();
    }

    public void loginUser(View v){

        if(email.getText().toString().equals("") && password.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Blank not allowed", Toast.LENGTH_SHORT).show();
        }else{
            auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                // Store login credentials
                                //CheckBox rememberMeCheckbox = findViewById(R.id.rememberme);

                                //rememberMeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->  {
                                SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                                editor.putString("pemail", email.getText().toString());
                                editor.putString("ppassword", password.getText().toString());
                                editor.apply();
                                //});

                                AuthResult acct = task.getResult();
                                String e = acct.getUser().getEmail();
                                String[] split = e.split("@");
                                Toast.makeText(getApplicationContext(),"User logged in succesfully",Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);

                            }else{
                                Toast.makeText(getApplicationContext(),"User could not be login",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    public void openRegister(View v)
    {
        Intent i = new Intent(getApplicationContext(), Signup.class);
        startActivity(i);
    }

    public void resetPassword(View v)
    {
        Intent i = new Intent(getApplicationContext(), Forgotpassword.class);
        startActivity(i);
    }
}