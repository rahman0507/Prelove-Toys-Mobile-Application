package com.example.liltoys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.widget.Button;
import android.widget.ImageView;
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

public class ProductDetail2 extends AppCompatActivity {

    TextView pname, pcategory, pdescription, plocation, pprice;
    ImageView pimage;
    Button addtocart, navigate;
    LocationRequest locationRequest;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail2);

        getSupportActionBar().hide();
        pname = findViewById(R.id.product_name2);
        pcategory = findViewById(R.id.product_category2);
        pdescription = findViewById(R.id.product_description2);
        plocation = findViewById(R.id.product_location2);
        pprice = findViewById(R.id.product_price2);
        pimage = findViewById(R.id.product_image2);
        addtocart = findViewById(R.id.add_to_cart_button2);
        navigate = findViewById(R.id.navigate_location_button2);

        Intent intent = getIntent();
        String getproductid = intent.getStringExtra("productID");
        String getproductlocation = intent.getStringExtra("productLocation");

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
                        Double productprice = products.getPrice();
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

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addedby = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference refProduct = database.getReference().child("Products");
                DatabaseReference refCart = database.getReference("Cart").push();

                //DATE
                // Get the current date
                Date currentDate = new Date();
                // Create a SimpleDateFormat object with the desired format
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                // Format the date as a string
                String dateString = dateFormat.format(currentDate);
                //Toast.makeText(getApplicationContext(),dateString,Toast.LENGTH_LONG).show();
                //DATE

                Query query = refProduct.orderByKey().equalTo(getproductid);
                Product [] product = new Product[1];
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            product[0] = childSnapshot.getValue(Product.class);

                            String uid = product[0].getUserid();
                            String key = childSnapshot.getKey();
                            String name = product[0].getName();
                            String cat = product[0].getCategory();
                            String desc = product[0].getDescription();
                            Double ppriced = product[0].getPrice();
                            String loc = product[0].getLocation();
                            String dateString = product[0].getDate();

                            DatabaseReference ref = database.getReference("Cart");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!snapshot.exists())
                                    {
                                        Cart cart = new Cart(uid, key, name, cat, desc, ppriced, loc, dateString, addedby);
                                        refCart.setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Intent i = new Intent(getApplicationContext(), Cartpage.class);
                                                    startActivity(i);
                                                }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                        {
                                            Cart c = dataSnapshot1.getValue(Cart.class);
                                            if(product[0].getProductid().equals(c.getProductid()))
                                            {
                                                Toast.makeText(getApplicationContext(),
                                                        "This product is already on your cart!",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                            else if(uid.equals(c.getUserid()))
                                            {
                                                Cart cart = new Cart(uid, key, name, cat, desc, ppriced, loc, dateString, addedby);
                                                refCart.setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Intent i = new Intent(getApplicationContext(), Cartpage.class);
                                                            startActivity(i);
                                                        }
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(),
                                                        "There is another product with different seller in cart. Please checkout or remove first!",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"Database error!",Toast.LENGTH_LONG).show();
                    }
                });


            }
        });



        navigate.setOnClickListener(new View.OnClickListener() {
            String latlong = null;
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(ActivityCompat.checkSelfPermission(ProductDetail2.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(isGPSEnabled())
                        {
                            LocationServices.getFusedLocationProviderClient(ProductDetail2.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(ProductDetail2.this).removeLocationUpdates(this);
                                    if(locationResult != null && locationResult.getLocations().size() > 0)
                                    {
                                        int index = locationResult.getLocations().size()-1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        latlong = latitude + ", " + longitude;
                                        Toast.makeText(ProductDetail2.this, latlong, Toast.LENGTH_SHORT).show();
                                        //TRACK//
                                        DisplayTrack(latlong,getproductlocation);
                                    }
                                }
                            }, Looper.getMainLooper());
                        }
                        else{
                            turnOnGps();
                        }
                    }
                    else
                    {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }
                }
            }
        });

    }

    private void DisplayTrack(String latlong, String destination) {
        try{
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/"+ latlong +"/"+destination);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void turnOnGps() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(ProductDetail2.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(ProductDetail2.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    private boolean isGPSEnabled()
    {
        LocationManager locationManager = null;
        boolean isEnabled = false;
        if(locationManager == null)
        {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }
}