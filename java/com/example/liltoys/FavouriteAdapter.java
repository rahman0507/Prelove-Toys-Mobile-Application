package com.example.liltoys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class FavouriteAdapter extends BaseAdapter {
    private Context context;
    private List<Cart> carts;

    public FavouriteAdapter(Context context, List<Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    @Override
    public int getCount() {
        return carts.size();
    }

    @Override
    public Object getItem(int position) {
        return carts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cart cart = carts.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.favitem, parent, false);
        }
        TextView name = convertView.findViewById(R.id.product_namefav);
        TextView price = convertView.findViewById(R.id.product_pricefav);
        ImageView imageView = convertView.findViewById(R.id.product_imagefav);

        String addedby = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(cart.getAddedby().equals(addedby))
        {
            name.setText(cart.getName());
            DecimalFormat df = new DecimalFormat("#.00");
            price.setText("RM"+df.format(cart.getPrice()));
            Glide.with(convertView).load("https://firebasestorage.googleapis.com/v0/b/liltoysfyp-9e150.appspot.com/o/images%2Fproducts%2F"+ cart.getProductid() +"?alt=media&token=a394fcd7-1577-4be1-bf43-a7cfa86de309").into(imageView);



        }

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail2.class);
                i.putExtra("productID", cart.getProductid());
                i.putExtra("productLocation", cart.getLocation());
                context.startActivity(i);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail2.class);
                i.putExtra("productID", cart.getProductid());
                i.putExtra("productLocation", cart.getLocation());
                context.startActivity(i);
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail2.class);
                i.putExtra("productID", cart.getProductid());
                i.putExtra("productLocation", cart.getLocation());
                context.startActivity(i);
            }
        });
        Button remove = convertView.findViewById(R.id.removefav);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carts.remove(position);
                notifyDataSetChanged();
                String id = cart.getProductid();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Favourites").orderByChild("productid").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
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


        return convertView;
    }
}
