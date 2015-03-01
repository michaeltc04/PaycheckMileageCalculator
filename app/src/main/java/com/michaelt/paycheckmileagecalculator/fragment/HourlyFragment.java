package com.michaelt.paycheckmileagecalculator.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.michaelt.paycheckmileagecalculator.R;
import com.michaelt.paycheckmileagecalculator.adapter.PayFragmentAdapter;

public class HourlyFragment extends Fragment {
    private View mView;
    private String[] status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_hourly, null);
        Spinner spinner = (Spinner) mView.findViewById(R.id.filing_status_spinner);
        status = new String[]{"Single", "Joint", "Married", "Head"};

        PayFragmentAdapter myAdapter = new PayFragmentAdapter(getActivity().getApplicationContext(), status);
        spinner.setAdapter(myAdapter);
        return mView;
    }
}