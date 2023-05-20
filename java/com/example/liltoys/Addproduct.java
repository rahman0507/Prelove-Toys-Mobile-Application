package com.example.liltoys;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Addproduct extends AppCompatActivity {

    FirebaseAuth auth;
    EditText pname, pprice, pdescription,pcategory, plocation;
    Spinner spinnerC, spinnerL;
    ImageView imageView;
    Button submit;
    FirebaseStorage storage;
    Uri imageUri;
    final String category [] = new String[]{"Option 1"};
    final String location [] = new String[]{"Option1"};
    static int id=0;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_addproduct);

        storage = FirebaseStorage.getInstance();

        imageView = (ImageView) findViewById(R.id.imageupload);
        submit = (Button) findViewById(R.id.UploadBtn);
        pname = (EditText) findViewById(R.id.toyname2);
        pprice = (EditText) findViewById(R.id.Price1);
        pdescription = (EditText)findViewById(R.id.Description1);



        spinnerC = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC.setAdapter(adapter);

        spinnerL = (Spinner)findViewById(R.id.spinnerlocation);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.location_options, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerL.setAdapter(adapter2);


        spinnerC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        //untuk pick image//
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });
    }

    private void uploadImage() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Products");
        DatabaseReference newRef = databaseReference.push();
        String key = newRef.getKey();


        final Uri [] url = new Uri[1];
        final String[] uuu = new String[1];




        StorageReference reference = storage.getReferenceFromUrl("gs://liltoysfyp-9e150.appspot.com").child("images/products/"+key+"");
        if(imageUri != null)
        {


            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Product Upload Successfully "+key,Toast.LENGTH_LONG).show();
                }
            });


            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    url[0] = uri;
                    Log.d(TAG,uri.toString());
                    uuu[0] = uri.toString();
                    Toast.makeText(getApplicationContext(), uuu[0],Toast.LENGTH_LONG).show();

                }
            });
        }

        String name = pname.getText().toString();
        String price = pprice.getText().toString();
        Double ppriced = Double.parseDouble(price);
        String desc = pdescription.getText().toString();
        String cat = category[0];
        String loc = location[0];
        // Get the current date
        Date currentDate = new Date();
        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Format the date as a string
        String dateString = dateFormat.format(currentDate);

        //USE THIS TO GET DATE STRING FROM FIREBASE AND CONVERT INTO DATE AND COMPARE//
        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateFormat.parse("01/01/2022");
        Date date2 = dateFormat.parse("01/01/2023");
        if(date1.compareTo(date2)<0){
            // date1 is before date2
        }else if(date1.compareTo(date2)>0){
            // date1 is after date2
        }else{
            // date1 is same as date2
        }
         */
        //

        Product product = new Product(uid, key, name, cat, desc, ppriced, loc, dateString);


        newRef.setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });


    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null)
                    {
                        imageView.setImageURI(result);
                        imageUri = result;
                    }
                }
            });
}