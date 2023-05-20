package com.example.liltoys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductProfileDetail extends AppCompatActivity {

    EditText pname, pdescription, pprice;
    TextView pcategory, plocation;
    ImageView pimage;
    Button edit, delete;
    LocationRequest locationRequest;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_profile_detail);

        getSupportActionBar().hide();
        pname = findViewById(R.id.product_name2);
        pcategory = findViewById(R.id.product_category2);
        pdescription = findViewById(R.id.product_description2);
        plocation = findViewById(R.id.product_location2);
        pprice = findViewById(R.id.product_price2);
        pimage = findViewById(R.id.product_image2);
        edit = findViewById(R.id.btnEdit);
        delete = findViewById(R.id.btnDelete);
        Button save = findViewById(R.id.btnSave);
        Button cancel = findViewById(R.id.btnCancel);
        save.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);

        pname.setEnabled(false);
        pprice.setEnabled(false);
        pdescription.setEnabled(false);

        Intent intent = getIntent();
        String getproductid = intent.getStringExtra("productID");
        //Toast.makeText(getApplicationContext(), getproductid + getproductlocation, Toast.LENGTH_LONG).show();

        if(getproductid != null)
        {
            Query query = databaseReference.orderByKey().equalTo(getproductid);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Product products = childSnapshot.getValue(Product.class);
                        String productname = products.getName();
                        pname.setText(productname);
                        DecimalFormat df = new DecimalFormat("#.00");
                        pprice.setText("Price RM:"+df.format(products.getPrice()));
                        pdescription.setText("Product Description: "+products.getDescription());
                        pcategory.setText("Category: "+products.getCategory());
                        plocation.setText("Location to Pickup: "+products.getLocation());
                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/liltoysfyp-9e150.appspot.com/o/images%2Fproducts%2F"+ products.getProductid() +"?alt=media&token=a394fcd7-1577-4be1-bf43-a7cfa86de309").into(pimage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"Database error!",Toast.LENGTH_LONG).show();
                }
            });
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pname.setEnabled(true);
                pprice.setEnabled(true);
                pdescription.setEnabled(true);
                save.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                edit.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
                //pname.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.));

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String names = pname.getText().toString();
                        //Double prices = Double.valueOf(pprice.getText().toString());
                        String descs = pdescription.getText().toString();

                        String price = pprice.getText().toString();
                        int endIndex = 14;
                        String p = price.substring(9, endIndex);
                        Double prices = Double.valueOf(p);

                        Query query = databaseReference.orderByKey().equalTo(getproductid);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                databaseReference.child(getproductid).child("name").setValue(names);
                                databaseReference.child(getproductid).child("price").setValue(prices);
                                databaseReference.child(getproductid).child("description").setValue(descs);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(),"Database error!",Toast.LENGTH_LONG).show();
                            }
                        });
                        Toast.makeText(ProductProfileDetail.this, "Product update successfully",Toast.LENGTH_SHORT).show();
                        save.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.INVISIBLE);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        save.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.INVISIBLE);
                        edit.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.VISIBLE);
                        pname.setEnabled(false);
                        pprice.setEnabled(false);
                        pdescription.setEnabled(false);
                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Products").orderByChild("productid").equalTo(getproductid).addListenerForSingleValueEvent(new ValueEventListener() {
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
                Intent d = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(d);
            }
        });
    }
}