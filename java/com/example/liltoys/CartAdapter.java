package com.example.liltoys;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Cart> mProductList;

    public CartAdapter(Context context, ArrayList<Cart> productList) {
        mContext = context;
        mProductList = productList;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cartpageitem, parent, false);
        }

        Cart cart = mProductList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.text_product_name);
        nameTextView.setText(cart.getName());

        TextView priceTextView = convertView.findViewById(R.id.text_product_price);
        DecimalFormat df = new DecimalFormat("#.00");
        priceTextView.setText("RM"+df.format(cart.getPrice()));


        ImageView imageCart = convertView.findViewById(R.id.imagecart);
        Glide.with(convertView).load("https://firebasestorage.googleapis.com/v0/b/liltoysfyp-9e150.appspot.com/o/images%2Fproducts%2F"+ cart.getProductid() +"?alt=media&token=a394fcd7-1577-4be1-bf43-a7cfa86de309").into(imageCart);

        Button remove = convertView.findViewById(R.id.button_remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductList.remove(position);
                notifyDataSetChanged();
                String id = cart.getProductid();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Cart").orderByChild("productid").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
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


