package com.example.liltoys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;

public class ExampleDialog extends AppCompatDialogFragment {
    Spinner datespinner, categoryspinner, min, max;
    private ExampleDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.nothing, null);


        builder.setView(view).setTitle("Logout?")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("login",Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();
                        Intent m = new Intent(getActivity(), Splashscreen.class);
                        startActivity(m);
                    }
                });
        return builder.create();

        /*
        datespinner = view.findViewById(R.id.datespinnerd);
        categoryspinner = view.findViewById(R.id.categoryspinnerd);
        min = view.findViewById(R.id.spinnermind);
        max = view.findViewById(R.id.spinnermaxd);

        final String [] datefilter = new String[1];
        final String [] categoryfilter = new String[1];
        final String [] minfilter = new String[1];
        final String [] maxfilter = new String[1];

        datespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                datefilter[0] = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryfilter[0] = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(getApplicationContext(),categoryfilter[0],Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        max.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maxfilter[0] = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minfilter[0] = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button search = view.findViewById(R.id.filterbtnd);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listener.applyTexts(datefilter[0],categoryfilter[0],minfilter[0],maxfilter[0]);
            }
        });

         */
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (ExampleDialogListener) context;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface ExampleDialogListener{
        void applyTexts(String datef);
    }
}
