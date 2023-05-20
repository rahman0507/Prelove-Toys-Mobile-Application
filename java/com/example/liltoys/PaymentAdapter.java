package com.example.liltoys;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PaymentAdapter extends ArrayAdapter<Checkout> {
    public PaymentAdapter(Context context, ArrayList<Checkout> payments) {
        super(context, 0, payments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Checkout payment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notificationsitem, parent, false);
        }

        TextView title = convertView.findViewById(R.id.notititle);
        TextView desc = convertView.findViewById(R.id.notidesc);
        TextView date = convertView.findViewById(R.id.notidate);

        title.setText("Payment Confirmed");
        SpannableString spannable = new SpannableString("Payment for order "+payment.getPaymentid()+" has been confirmed and we've notified the seller. Kindly proceed with the pickup!");
        spannable.setSpan(new ForegroundColorSpan(getContext().getColor(R.color.brown)), 19, 19 + payment.getPaymentid().length(), 0);
        desc.setText(spannable);
        desc.setText(spannable);
        date.setText(payment.getDate());

        return convertView;
    }
}
