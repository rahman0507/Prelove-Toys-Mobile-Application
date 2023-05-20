package com.example.liltoys.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import androidx.appcompat.widget.SearchView;
//import android.widget.SearchView;
import android.widget.Toast;
//import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.liltoys.Cartpage;
import com.example.liltoys.ExampleDialog;
import com.example.liltoys.Filterpage;
import com.example.liltoys.Product;
import com.example.liltoys.ProductAdapter;
import com.example.liltoys.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ExampleDialog.ExampleDialogListener {

    private List<Product> products = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Products");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        String [] date = new String[1];
        String [] category = new String[1];
        String [] min = new String[1];
        String [] max = new String[1];
        if(getArguments()!=null)
        {
            min[0] = getArguments().getString("datefilter");
            Toast.makeText(getActivity(),min[0],Toast.LENGTH_LONG).show();
        }

        ImageButton opencart = view.findViewById(R.id.cartimg);
        opencart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Cartpage.class);
                startActivity(i);
            }
        });


        ImageButton openfilter = view.findViewById(R.id.filter);
        openfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.fragment_filter_dialog);
                dialog.show();
                FilterDialogFragment filterDialog = new FilterDialogFragment();
                filterDialog.show(getChildFragmentManager(), "filter_dialog");
                if(getActivity() instanceof OnDataChangedListener)
                {
                    mListener = (DatePicker.OnDateChangedListener) getActivity();
                }
                 */

                Intent intent = new Intent(getActivity(),Filterpage.class);
                startActivity(intent);
                //openDialog();
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if(getArguments() != null)
        {
            String date1 = getArguments().getString("datefilter");
            String category1 = getArguments().getString("categoryfilter");
            int mins = Integer.parseInt(getArguments().getString("minfilter"));
            int maxs = Integer.parseInt(getArguments().getString("maxfilter"));
            Toast.makeText(getActivity(),date[0],Toast.LENGTH_LONG).show();

            GridView gridView = view.findViewById(R.id.gridview);
            DatabaseReference myreference = FirebaseDatabase.getInstance().getReference("Products");
            Query q = myreference.orderByChild("category").equalTo(category1);
            q.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    products.clear();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Product product = childSnapshot.getValue(Product.class);
                        products.add(product);
                    }
                    gridView.setAdapter(new ProductAdapter(getContext(), products));
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error here
                }
            });
        }
        else
        {
            GridView gridView = view.findViewById(R.id.gridview);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    products.clear();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Product product = childSnapshot.getValue(Product.class);
                        products.add(product);
                    }
                    gridView.setAdapter(new ProductAdapter(getContext(), products));
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error here
                }
            });
        }


        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                GridView gridView = view.findViewById(R.id.gridview);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        products.clear();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            Product product = childSnapshot.getValue(Product.class);
                            if(product.getName().contains(query) || product.getName().toLowerCase().contains(query.toLowerCase()))
                            {
                                products.add(product);
                            }

                        }
                        gridView.setAdapter(new ProductAdapter(getContext(), products));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle error here
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the text change event
                GridView gridView = view.findViewById(R.id.gridview);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        products.clear();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            Product product = childSnapshot.getValue(Product.class);
                            if(product.getName().contains(newText) || product.getName().toLowerCase().contains(newText.toLowerCase()))
                            {
                                products.add(product);
                            }

                        }
                        gridView.setAdapter(new ProductAdapter(getContext(), products));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle error here
                    }
                });

                return false;
            }
        });



        return view;
    }

    public static HomeFragment newInstance(String date, String category, String min, String max) {
        HomeFragment myFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("datefilter", date);
        args.putString("categoryfilter", category);
        args.putString("minfilter", min);
        args.putString("maxfilter", max);
        myFragment.setArguments(args);
        return myFragment;
    }

    public void openDialog()
    {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getChildFragmentManager(), "Example Dialog");
    }

    @Override
    public void applyTexts(String datef) {
        Toast.makeText(getActivity(),datef,Toast.LENGTH_LONG).show();
    }
}