package com.example.liltoys.ui.favourite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.liltoys.Cart;
import com.example.liltoys.FavouriteAdapter;
import com.example.liltoys.Product;
import com.example.liltoys.ProductAdapter;
import com.example.liltoys.R;
import com.example.liltoys.Splashscreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private List<Cart> carts = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Favourites");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        GridView gridView = view.findViewById(R.id.gridviewfavourite);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //products.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Cart cart = childSnapshot.getValue(Cart.class);
                    carts.add(cart);
                }
                gridView.setAdapter(new FavouriteAdapter(getContext(), carts));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error here
            }
        });




        /* //Logout Button Function
        Button logout = view.findViewById(R.id.buttonLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("login",Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                Intent i = new Intent(getActivity(), Splashscreen.class);
                startActivity(i);
            }
        });
         */
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}