package com.example.liltoys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.liltoys.ui.home.HomeFragment;

public class Filterpage extends AppCompatActivity {

    Spinner datespinner, categoryspinner, min, max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterpage);

        getSupportActionBar().hide();
        datespinner = findViewById(R.id.datespinner);
        categoryspinner = findViewById(R.id.categoryspinner);
        min = findViewById(R.id.spinnermin);
        max = findViewById(R.id.spinnermax);

        String [] datefilter = new String[1];
        String [] categoryfilter = new String[1];
        String [] minfilter = new String[1];
        String [] maxfilter = new String[1];

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

        Button search = findViewById(R.id.filterbtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*HomeFragment filter = HomeFragment.newInstance(datefilter[0],categoryfilter[0],minfilter[0], maxfilter[0]);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                 */

                String data = datefilter[0]+" "+categoryfilter[0]+" "+minfilter[0]+" "+maxfilter[0];
                Intent resultIntent = new Intent();
                resultIntent.putExtra("filterstring", data);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });




    }
}