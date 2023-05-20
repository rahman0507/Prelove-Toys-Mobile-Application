package com.example.liltoys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Paymentdetail extends AppCompatActivity {

    DatabaseReference refPayment = FirebaseDatabase.getInstance().getReference("Payment");
    DatabaseReference refProduct = FirebaseDatabase.getInstance().getReference("Cart");
    Query qpay, qproduct;
    //private List<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentdetail);
        getSupportActionBar().hide();

        Intent i = getIntent();
        String paymentid = i.getStringExtra("paymentid");

        TextView sellername = findViewById(R.id.payname);
        TextView selleraddress = findViewById(R.id.payaddress);
        TextView paymentmethod = findViewById(R.id.paymethod);
        TextView paymentdate = findViewById(R.id.paydate);
        //ListView list = findViewById(R.id.detaillistview);

        final ListView listView = findViewById(R.id.detaillistview);
        final ArrayList<Cart> products = new ArrayList<>();
        final PaymentdetailAdapter adapter = new PaymentdetailAdapter(getApplicationContext(), products);
        listView.setAdapter(adapter);

        qpay = refPayment.orderByKey().equalTo(paymentid);
        qpay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Checkout c = dataSnapshot.getValue(Checkout.class);
                    sellername.setText("Name "+c.getName());
                    selleraddress.setText("Address: "+c.getAddress());
                    paymentmethod.setText("Payment Method: "+c.getPaymentmethod());
                    paymentdate.setText("Date Payment: "+c.getDate());
                    String userid = c.getAddedby();
                    qproduct = refProduct.orderByChild("addedby").equalTo(userid);
                    qproduct.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            for(DataSnapshot dataSnapshot1 : snapshot1.getChildren())
                            {
                                Cart p = dataSnapshot1.getValue(Cart.class);
                                //products.add(p);
                                products.add(new Cart(p.getUserid(),p.getProductid(),p.getName(),p.getCategory(),p.getDescription(),p.getPrice(),p.getLocation(),p.getDate(),p.getAddedby()));
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                //list.setAdapter(new PaymentdetailAdapter(Paymentdetail.this,products));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button orderreceived = findViewById(R.id.receivedpaybutton);
        orderreceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Paymentdetail.this);
                builder.setMessage("Order Completed!");
                AlertDialog alert = builder.create();
                alert.show();
                qpay = refPayment.orderByKey().equalTo(paymentid);
                qpay.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            dataSnapshot.getRef().removeValue();
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