package com.example.liltoys.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liltoys.Addproduct;
import com.example.liltoys.ExampleDialog;
import com.example.liltoys.Product;
import com.example.liltoys.ProductProfileAdapter;
import com.example.liltoys.R;
import com.example.liltoys.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        //FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users");
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ImageButton logout = view.findViewById(R.id.morebutton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


        TextView name = view.findViewById(R.id.proname);

        Query query = myRef.orderByKey().equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Users users = childSnapshot.getValue(Users.class);
                    name.setText(users.getFullname());

                    //something ambik url image
                    //grindle
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Database error!",Toast.LENGTH_LONG).show();
            }
        });

//sellprods
        Button addprods = view.findViewById(R.id.sellprods);

        addprods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Addproduct.class);
                startActivity(intent);
            }
        });

        //product list start
        List<Product> products = new ArrayList<>();
        GridView grid = view.findViewById(R.id.gridviewprofile);
        DatabaseReference refProduct = FirebaseDatabase.getInstance().getReference("Products");
        String key = refProduct.push().getKey();

        Query query1 = refProduct.orderByChild("userid").equalTo(uid);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Product p = dataSnapshot.getValue(Product.class);
                    products.add(p);
                }
                grid.setAdapter(new ProductProfileAdapter(getContext(), products));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //product list end

        return view;
    }

    public void openDialog()
    {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getParentFragmentManager(), "Logout?");
    }
}