package com.example.liltoys.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.liltoys.Checkout;
import com.example.liltoys.PaymentAdapter;
import com.example.liltoys.Paymentdetail;
import com.example.liltoys.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment {

    private List<Checkout> products = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Payments");

    private ArrayList<Checkout> arrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        final ListView listView = view.findViewById(R.id.notilistview);
        final ArrayList<Checkout> payments = new ArrayList<>();
        final PaymentAdapter adapter = new PaymentAdapter(getContext(), payments);
        listView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference paymentRef = database.getReference("Payment");
        Query q = paymentRef.orderByChild("addedby").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                payments.clear();

                if(!dataSnapshot.exists())
                {
                    TextView name = view.findViewById(R.id.notinull);
                    name.setText("You don't have notifications.");
                }
                else
                {
                    for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
                        Checkout c = paymentSnapshot.getValue(Checkout.class);
                        payments.add(new Checkout(c.getPaymentid(),c.getAddress(),c.getPaymentmethod(),c.getAddedby(),c.getUserid(),c.getPaymentid(),c.getDate()));
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Checkout c = payments.get(i);
                Intent intent = new Intent(getActivity(), Paymentdetail.class);
                intent.putExtra("paymentid", c.getPaymentid());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}