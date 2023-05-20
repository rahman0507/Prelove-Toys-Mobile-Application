package com.example.liltoys;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.liltoys.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FilterDialogFragment extends AppCompatDialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_dialog, container, false);

        String [] datefilter = new String[1];
        Spinner datespinner = view.findViewById(R.id.datespinner);
        datespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                datefilter[0] = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getActivity(),datefilter[0],Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Products");
        List<Product> products = new ArrayList<>();

        Button search = view.findViewById(R.id.filterbtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),datefilter[0],Toast.LENGTH_LONG).show();

                /*
                if(datefilter[0].equals("Sort by newest"))
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
                            Collections.sort(products, new Comparator<Product>() {
                                @Override
                                public int compare(Product product, Product t1) {
                                    return product.getDate().compareTo(t1.getDate());
                                }
                            });
                            gridView.setAdapter(new ProductAdapter(getContext(), products));
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle error here
                        }
                    });
                }

                 */
                dismiss();
            }
        });

        return view;
    }
}