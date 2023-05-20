package com.example.liltoys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PaymentdetailAdapter extends ArrayAdapter<Cart> {
    public PaymentdetailAdapter(Context context, ArrayList<Cart> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Cart product = getItem(i);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.paymentdetailitem, viewGroup, false);
        }

        TextView name = view.findViewById(R.id.productnamepayitem);
        name.setText("ProductName: "+product.getName());
        TextView price = view.findViewById(R.id.productpricepayitem);
        price.setText("RM: "+product.getPrice());

        ImageView imageView = view.findViewById(R.id.imageView3);
        Glide.with(view).load("https://firebasestorage.googleapis.com/v0/b/liltoysfyp-9e150.appspot.com/o/images%2Fproducts%2F"+ product.getProductid() +"?alt=media&token=a394fcd7-1577-4be1-bf43-a7cfa86de309").into(imageView);

        /*Product product = products.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.paymentdetailitem, viewGroup, false);
        }

        TextView name = view.findViewById(R.id.productnamepayitem);
        name.setText(product.getName());
        TextView price = view.findViewById(R.id.productpricepayitem);
        price.setText("RM: ");
         */


        return view;
    }
}
