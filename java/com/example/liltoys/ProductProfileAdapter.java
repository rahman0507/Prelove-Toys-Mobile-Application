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

public class ProductProfileAdapter extends BaseAdapter {
    private Context context;
    private List<Product> products;

    //untuk

    public ProductProfileAdapter(Context context, List<Product> products) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.productprofileitem, parent, false);
        }
        TextView name = convertView.findViewById(R.id.product_nameprof);
        name.setText(product.getName());
        TextView price = convertView.findViewById(R.id.product_priceprof);
        DecimalFormat df = new DecimalFormat("#.00");
        price.setText("RM"+df.format(product.getPrice()));
        ImageView imageView = convertView.findViewById(R.id.product_imageprof);
        Glide.with(convertView).load("https://firebasestorage.googleapis.com/v0/b/liltoysfyp-9e150.appspot.com/o/images%2Fproducts%2F"+ product.getProductid() +"?alt=media&token=a394fcd7-1577-4be1-bf43-a7cfa86de309").into(imageView);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductProfileDetail.class);
                i.putExtra("productID", product.getProductid());
                i.putExtra("productLocation", product.getLocation());
                context.startActivity(i);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductProfileDetail.class);
                i.putExtra("productID", product.getProductid());
                i.putExtra("productLocation", product.getLocation());
                context.startActivity(i);
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductProfileDetail.class);
                i.putExtra("productID", product.getProductid());
                i.putExtra("productLocation", product.getLocation());
                context.startActivity(i);
            }
        });

        return convertView;
    }
}
