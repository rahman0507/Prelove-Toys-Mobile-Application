package com.example.liltoys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText fname, emailsignup, passsignup, cpass, phone;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView btn = (TextView)findViewById(R.id.login);
        fname = (EditText) findViewById(R.id.inputfullname);
        phone = (EditText)findViewById(R.id.inputphone);
        emailsignup = (EditText) findViewById(R.id.inputemail);
        passsignup = (EditText) findViewById(R.id.inputpassword);
        cpass = (EditText) findViewById(R.id.inputconfirmpasssword);
        auth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

    }

    public void registerUser(View v)
    {
        String email2 = emailsignup.getText().toString();
        String phone2 = phone.getText().toString();
        String fname2 = fname.getText().toString();
        String password2 = passsignup.getText().toString();
        String cpass2 = cpass.getText().toString();
        if(emailsignup.getText().toString().equals("") && passsignup.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please enter email or password", Toast.LENGTH_SHORT).show();
        }
        else
        {


            if(password2.equals(cpass2))
            {
                auth.createUserWithEmailAndPassword(email2,password2)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"User registered successfully", Toast.LENGTH_SHORT).show();
                                    finish();

                                    Users user = new Users(fname2, phone2, email2, password2);
                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(getApplicationContext(),"User registered successfully", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(getApplicationContext(), Login.class);
                                                        startActivity(i);
                                                    }
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Sign up is unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        }
    }

    public void openlogin(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
    }
}