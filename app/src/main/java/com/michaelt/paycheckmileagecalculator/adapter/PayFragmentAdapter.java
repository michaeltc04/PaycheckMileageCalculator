package com.michaelt.paycheckmileagecalculator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.michaelt.paycheckmileagecalculator.R;


public class PayFragmentAdapter extends ArrayAdapter {

    private Context context;
    private String[] status;

    public PayFragmentAdapter(Context context, String[] status) {
        super(context, R.layout.status_item, status);
        this.context = context;
        this.status = status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.status_item, null);
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.filing_status_item);
        text.setText(status[position]);
        return view;
    }
}
