package com.example.liltoys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.liltoys.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = products.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.homeitem, parent, false);
        }
        TextView name = convertView.findViewById(R.id.product_name);
        name.setText(product.getName());
        TextView price = convertView.findViewById(R.id.product_price);
        DecimalFormat df = new DecimalFormat("#.00");
        price.setText("RM"+df.format(product.getPrice()));
        ImageView imageView = convertView.findViewById(R.id.product_image);
        Glide.with(convertView).load("https://firebasestorage.googleapis.com/v0/b/liltoysfyp-9e150.appspot.com/o/images%2Fproducts%2F"+ product.getProductid() +"?alt=media&token=a394fcd7-1577-4be1-bf43-a7cfa86de309").into(imageView);

        ImageView fav = convertView.findViewById(R.id.favourite_icon);
        //baca database and if the item in fav, change the color of the icon

        Cart [] favs = new Cart[1];
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference refProduct = FirebaseDatabase.getInstance().getReference("Products");
                DatabaseReference refFav = FirebaseDatabase.getInstance().getReference("Favourites").push();
                String addedby = FirebaseAuth.getInstance().getCurrentUser().getUid();

                refProduct.orderByKey().equalTo(product.getProductid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            favs[0] = dataSnapshot.getValue(Cart.class);
                            String uid = favs[0].getUserid();
                            String key = dataSnapshot.getKey();
                            String name = favs[0].getName();
                            String cat = favs[0].getCategory();
                            String desc = favs[0].getDescription();
                            Double ppriced = favs[0].getPrice();
                            String loc = favs[0].getLocation();
                            String dateString = favs[0].getDate();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Favourites");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                    if(!snapshot2.exists())
                                    {
                                        Cart cart = new Cart(uid, key, name, cat, desc, ppriced, loc, dateString, addedby);
                                        refFav.setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(context,
                                                            "Items added to your favourite!",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        for(DataSnapshot dataSnapshot1 : snapshot2.getChildren())
                                        {
                                            Cart c = dataSnapshot1.getValue(Cart.class);
                                            if(favs[0].getProductid().equals(c.getProductid()))
                                            {
                                                Toast.makeText(context,
                                                        "This product is already on your favourite!",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                            else if(!favs[0].getProductid().equals(c.getProductid()))
                                            {
                                                Cart cart = new Cart(uid, key, name, cat, desc, ppriced, loc, dateString, addedby);
                                                refFav.setValue(cart);
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

                    }
                });

            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail2.class);
                i.putExtra("productID", product.getProductid());
                i.putExtra("productLocation", product.getLocation());
                context.startActivity(i);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail2.class);
                i.putExtra("productID", product.getProductid());
                i.putExtra("productLocation", product.getLocation());
                context.startActivity(i);
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail2.class);
                i.putExtra("productID", product.getProductid());
                i.putExtra("productLocation", product.getLocation());
                context.startActivity(i);
            }
        });

        return convertView;
    }
}
