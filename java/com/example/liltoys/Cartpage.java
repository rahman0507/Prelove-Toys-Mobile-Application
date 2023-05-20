package com.example.liltoys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Cartpage extends AppCompatActivity {

    private ArrayList<Cart> mProductList;
    private CartAdapter mAdapter;
    ListView cartListView;
    TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartpage);
        getSupportActionBar().hide();

        cartListView = findViewById(R.id.list_view_cart);
        totalPrice = findViewById(R.id.text_product_total);

        mProductList = new ArrayList<>();
        mAdapter = new CartAdapter(Cartpage.this, mProductList);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Cart");

        Query query = databaseReference.orderByChild("addedby").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProductList.clear();
                for(DataSnapshot childSnapshot: snapshot.getChildren())
                {
                    Cart cart = childSnapshot.getValue(Cart.class);
                    mProductList.add(cart);
                }
                cartListView.setAdapter(new CartAdapter(getApplicationContext(), mProductList));
                mAdapter.notifyDataSetChanged();
                updateTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database Error in CartPage!", Toast.LENGTH_LONG).show();
            }
        });

        Button checkout = findViewById(R.id.checkout_button);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CheckoutPage.class);
                startActivity(i);
            }
        });

    }

    public void updateTotal() {
        double total = 0;
        for (Cart cart : mProductList) {

                total += cart.getPrice();
        }
        DecimalFormat df = new DecimalFormat("#.00");
        totalPrice.setText("Total: RM" + df.format(total));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}