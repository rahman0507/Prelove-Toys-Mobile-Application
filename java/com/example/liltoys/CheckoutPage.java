package com.example.liltoys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.liltoys.ui.notifications.NotificationsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckoutPage extends AppCompatActivity {

    EditText name,address,phone;
    String paymentmethod;
    RadioGroup radioGroup;
    RadioButton r1, r2, r3, r4, radioPaymentButton;
    Button placeorder;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();


    DatabaseReference refPayment = FirebaseDatabase.getInstance().getReference("Payment").push();
    String paymentid = refPayment.getKey();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);
        getSupportActionBar().hide();

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phonenumber);
        placeorder = findViewById(R.id.proceedbtn);

        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("Users");
        refUser.orderByKey().equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Users u = dataSnapshot.getValue(Users.class);
                    name.setText(u.getFullname());
                    phone.setText(u.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        radioGroup = findViewById(R.id.payment_group);
        r1 = findViewById(R.id.credit_card);
        r2 = findViewById(R.id.debit_card);
        r3 = findViewById(R.id.onlinebanking);
        r4 = findViewById(R.id.pickup);
        String p;


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioPaymentButton = findViewById(i);
                paymentmethod = radioPaymentButton.getText().toString();
            }
        });

        //create new db checkout, take all item from cart and make it nodes in checkout db


        final Integer[] count = {0};
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refCart = database.getReference("Cart");


        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refCart.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Cart c = dataSnapshot.getValue(Cart.class);
                            String userid = c.getUserid(); //owner barang

                            Date currentDate = new Date();
                            // Create a SimpleDateFormat object with the desired format
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            // Format the date as a string
                            String dateString = dateFormat.format(currentDate);

                            Checkout co = new Checkout(name.getText().toString(),address.getText().toString(),paymentmethod, uid, userid,paymentid,dateString);
                            refPayment.setValue(co);
                            dataSnapshot.getRef().removeValue();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutPage.this);
                            builder.setMessage("Payment Completed!");
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}